package com.example.roomapp.controller;

import com.example.roomapp.config.Credentials;
import com.example.roomapp.config.KeycloakConfig;
import com.example.roomapp.security.JwtConverter;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class LoginController {

    private final JwtConverter jwtConverter;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public LoginController(JwtConverter jwtConverter, JwtDecoder jwtDecoder) {
        this.jwtConverter = jwtConverter;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/roles")
    public Map<String, Object> getUserDetailsAndToken(@RequestBody Map<String, String> request) {
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
                "grant_type=password&client_secret=KXXZIpd6Habx8WgDtmsGT3clSFJccURk&client_id=myapp-api&username=%s&password=%s",
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

        // Step 2: Fetch user details from Keycloak
        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        List<UserRepresentation> users = usersResource.search(username, true);
        if (users.isEmpty()) {
            throw new RuntimeException("User not found in Keycloak.");
        }
        UserRepresentation user = users.get(0);

        // Extract imageLink from user's attributes
        List<String> imageLink = null;
        if (user.getAttributes() != null && user.getAttributes().containsKey("imageLink")) {
            imageLink = user.getAttributes().get("imageLink");
        }

        // Step 3: Return Access Token and User Details without roles
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", accessToken);
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("imageLink", imageLink);

        return response;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (username == null || oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Username, old password, and new password are required.");
        }

        // Step 1: Authenticate user with old password
        String tokenEndpoint = "http://localhost:8080/realms/myapp/protocol/openid-connect/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format(
                "grant_type=password&client_secret=KXXZIpd6Habx8WgDtmsGT3clSFJccURk&client_id=myapp-api&username=%s&password=%s",
                username, oldPassword
        );

        HttpEntity<String> tokenRequest = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                    tokenEndpoint, HttpMethod.POST, tokenRequest, Map.class);

            if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password.");
        }

        // Step 2: Fetch user ID from Keycloak
        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        List<UserRepresentation> users = usersResource.search(username, true);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        String userId = users.get(0).getId();

        // Step 3: Update password
        CredentialRepresentation newCredential = Credentials.createPasswordCredentials(newPassword);
        UserRepresentation user = new UserRepresentation();
        user.setCredentials(Collections.singletonList(newCredential));

        usersResource.get(userId).update(user);

        return ResponseEntity.ok("Password updated successfully.");
    }
}