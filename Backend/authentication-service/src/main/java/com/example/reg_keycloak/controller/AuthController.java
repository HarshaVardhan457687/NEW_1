package com.example.reg_keycloak.controller;

import com.example.reg_keycloak.security.JwtConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.example.reg_keycloak.DTO.UserDTO;
import com.example.reg_keycloak.config.KeycloakConfig;
import com.example.reg_keycloak.service.KeyCloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.stream.Collectors;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static com.example.reg_keycloak.config.KeycloakConfig.realm;

@RestController
@CrossOrigin(origins = "http://localhost:8060")
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtConverter jwtConverter;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public AuthController(JwtConverter jwtConverter, JwtDecoder jwtDecoder) {
        this.jwtConverter = jwtConverter;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token is required"));
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);
            Collection<String> roles = jwtConverter.convert(jwt).getAuthorities().stream()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("roles", roles);
            response.put("subject", jwt.getSubject());
            response.put("issuedAt", jwt.getIssuedAt());
            response.put("expiresAt", jwt.getExpiresAt());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid or expired token"));
        }
    }

    @PostMapping("/authenticate")
    public Map<String, Object> getRolesAndToken(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        // Step 1: Fetch Access Token
        String tokenEndpoint = "http://localhost:8080/realms/myapp/protocol/openid-connect/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format(
                "grant_type=password&client_secret=ZuzzOLKark3CzbCGVYNbIUgEUOtgwEF4&client_id=myapp-api&username=%s&password=%s",
                username,
                password
        );

        HttpEntity<String> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                tokenEndpoint, HttpMethod.POST, tokenRequest, Map.class);

        

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to fetch access token: " + tokenResponse.getBody());
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // Step 2: Decode JWT and Extract Roles
        Jwt jwt = jwtDecoder.decode(accessToken);

        // Use the JwtConverter to extract the roles from the decoded JWT
        Collection<String> roles = jwtConverter.convert(jwt).getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))  // Remove "ROLE_" prefix
                .collect(Collectors.toList());


        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        List<UserRepresentation> users = usersResource.search(username, true);
        if (users.isEmpty()) {
             throw new RuntimeException("User not found in Keycloak.");
        }

        UserRepresentation user = users.get(0);

        List<String> name = null;
        if (user.getAttributes() != null && user.getAttributes().containsKey("name")) {
            name = user.getAttributes().get("name");
        }
        
        // Step 3: Return Access Token and Roles
        Map<String, Object> response = new HashMap<>();
        response.put("token", accessToken);
        response.put("roles", roles);
        response.put("name", name);

        return response;
    }
    
    @Autowired
    KeyCloakService service;

    @PostMapping
    public ResponseEntity<List<UserRepresentation>>  addUser(@RequestBody UserDTO userDTO){
        List<UserRepresentation> user1 = service.addUser(userDTO);
        return ResponseEntity.ok(user1);
    }

    @GetMapping(path = "/{userName}")
    public List<UserRepresentation> getUser(@PathVariable("userName") String userName){
        List<UserRepresentation> user = service.getUser(userName);
        return user;
    }

    @PutMapping(path = "/update/{userId}")
    public String updateUser(@PathVariable("userId") String userId, @RequestBody UserDTO userDTO){
        service.updateUser(userId, userDTO);
        return "User Details Updated Successfully.";
    }

    @DeleteMapping(path = "/delete/{userId}")
    public String deleteUser(@PathVariable("userId") String userId){
        service.deleteUser(userId);
        return "User Deleted Successfully.";
    }

    @PostMapping(value = "/users/{id}/roles/{roleName}")
    public Response createRole(@PathVariable("id") String id,
                               @PathVariable("roleName") String roleName) {
        Keycloak keycloak = KeycloakConfig.getInstance();
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(role));
        return Response.ok().build();
    }



    @GetMapping(path = "/verification-link/{userId}")
    public ResponseEntity<String> sendVerificationLink(@PathVariable("userId") String userId) {
        service.sendVerificationLink(userId);
        return ResponseEntity.ok("Verification Link Sent to Registered E-mail Id.");
    }

    @GetMapping(path = "/reset-password/{userId}")
    public ResponseEntity<String> sendResetPassword(@PathVariable("userId") String userId) {
        service.sendResetPassword(userId);
        return ResponseEntity.ok("Reset Password Link Sent Successfully to Registered E-mail Id.");
    }

    
}
