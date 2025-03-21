package com.user.user_service.entity;

import com.user.user_service.constants.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "complex-long-id-generator")
    @GenericGenerator(name = "complex-long-id-generator", strategy = "com.user.user_service.util.ComplexLongIdGenerator")
    private Long userId;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, unique = true, length = 150)
    private String userEmail;

    private String userDesignation;

    private String userProfilePhoto; // Cloudinary link

    private Long userPhoneNo;

    private String userAddress;

    private String userTimeZone;

    @Temporal(TemporalType.DATE)
    private Date userJoiningDate;

    private Boolean userIsNotificationAlert;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection
    private List<Long> userTaskAssigned;

    @ElementCollection
    private List<Long> userInvolvedTeamsId;

    // for role based projectId list
    @ElementCollection
    private List<Long> userManagerProjectId;
    @ElementCollection
    private List<Long> userTeamLeaderProjectId;
    @ElementCollection
    private List<Long> userTeamMemberProjectId;


    //GETTERS AND SETTERS
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public Long getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(Long userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserTimeZone() {
        return userTimeZone;
    }

    public void setUserTimeZone(String userTimeZone) {
        this.userTimeZone = userTimeZone;
    }

    public Date getUserJoiningDate() {
        return userJoiningDate;
    }

    public void setUserJoiningDate(Date userJoiningDate) {
        this.userJoiningDate = userJoiningDate;
    }

    public Boolean getUserIsNotificationAlert() {
        return userIsNotificationAlert;
    }

    public void setUserIsNotificationAlert(Boolean userIsNotificationAlert) {
        this.userIsNotificationAlert = userIsNotificationAlert;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public List<Long> getUserTaskAssigned() {
        return userTaskAssigned;
    }

    public void setUserTaskAssigned(List<Long> userTaskAssigned) {
        this.userTaskAssigned = userTaskAssigned;
    }


    public List<Long> getUserInvolvedTeamsId() {
        return userInvolvedTeamsId;
    }

    public void setUserInvolvedTeamsId(List<Long> userInvolvedTeamsId) {
        this.userInvolvedTeamsId = userInvolvedTeamsId;
    }

    public List<Long> getUserManagerProjectId() {
        return userManagerProjectId;
    }

    public void setUserManagerProjectId(List<Long> userManagerProjectId) {
        this.userManagerProjectId = userManagerProjectId;
    }

    public List<Long> getUserTeamLeaderProjectId() {
        return userTeamLeaderProjectId;
    }

    public void setUserTeamLeaderProjectId(List<Long> userTeamLeaderProjectId) {
        this.userTeamLeaderProjectId = userTeamLeaderProjectId;
    }

    public List<Long> getUserTeamMemberProjectId() {
        return userTeamMemberProjectId;
    }

    public void setUserTeamMemberProjectId(List<Long> userTeamMemberProjectId) {
        this.userTeamMemberProjectId = userTeamMemberProjectId;
    }
}


