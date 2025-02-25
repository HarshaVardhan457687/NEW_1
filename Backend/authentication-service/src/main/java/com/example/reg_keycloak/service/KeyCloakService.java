package com.example.reg_keycloak.service;

import com.example.reg_keycloak.config.Credentials;
import com.example.reg_keycloak.DTO.UserDTO;
import com.example.reg_keycloak.config.KeycloakConfig;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakService {
    private static final Logger logger = LoggerFactory.getLogger(KeyCloakService.class);

    public List<UserRepresentation> addUser(UserDTO userDTO){
        logger.info("Adding user: {}", userDTO.getUserName());
        CredentialRepresentation credential = Credentials.createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setAttributes(Map.of("name", Collections.singletonList(userDTO.getName())));
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource instance = getInstance();
        try {
            instance.create(user);
            logger.info("User {} created successfully", userDTO.getUserName());
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
        }

        List<UserRepresentation> user1 = instance.search(userDTO.getUserName(), true);
        return user1;
    }

    public List<UserRepresentation> getUser(String userName){
        logger.info("Fetching user: {}", userName);
        UsersResource usersResource = getInstance();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;
    }

    public void updateUser(String userId, UserDTO userDTO){
        logger.info("Updating user with ID: {}", userId);
        CredentialRepresentation credential = Credentials.createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setAttributes(Map.of("name", Collections.singletonList(userDTO.getName())));
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getInstance();
        try {
            usersResource.get(userId).update(user);
            logger.info("User with ID {} updated successfully", userId);
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage());
        }
    }

    public void deleteUser(String userId){
        logger.info("Deleting user with ID: {}", userId);
        UsersResource usersResource = getInstance();
        try {
            usersResource.get(userId).remove();
            logger.info("User with ID {} deleted successfully", userId);
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage());
        }
    }

    public void sendVerificationLink(String userId){
        logger.info("Sending verification link to user ID: {}", userId);
        UsersResource usersResource = getInstance();
        try {
            usersResource.get(userId).sendVerifyEmail();
            logger.info("Verification link sent to user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error sending verification link: {}", e.getMessage());
        }
    }

    public void sendResetPassword(String userId){
        logger.info("Sending reset password email to user ID: {}", userId);
        UsersResource usersResource = getInstance();
        try {
            usersResource.get(userId).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
            logger.info("Password reset email sent to user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error sending reset password email: {}", e.getMessage());
        }
    }

    public UsersResource getInstance(){
        logger.info("Fetching Keycloak user resource instance");
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
    }
}
