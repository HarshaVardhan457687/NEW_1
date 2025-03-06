package com.team.team_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "complex-long-id-generator")
    @GenericGenerator(name = "complex-long-id-generator", strategy = "com.team.team_service.util.ComplexLongIdGenerator")
    private Long teamId;
    private String teamName;
    private Long teamLead;
    private List<Long> teamMembers;
    private Long assignedProject;
    private List<Long> assignedKeyResult;

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(Long teamLead) {
        this.teamLead = teamLead;
    }

    public List<Long> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Long> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Long getAssignedProject() {
        return assignedProject;
    }

    public void setAssignedProject(Long assignedProject) {
        this.assignedProject = assignedProject;
    }

    public List<Long> getAssignedKeyResult() {
        return assignedKeyResult;
    }

    public void setAssignedKeyResult(List<Long> assignedKeyResult) {
        this.assignedKeyResult = assignedKeyResult;
    }
}
