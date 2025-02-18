package com.example.reg_keycloak.DTO;

import com.sun.istack.NotNull;

public class UserDTO {
    private String userName;
    private String emailId;
    private String password;
    private String name;
    

    public UserDTO(String userName, String emailId, String password, String name) {
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
