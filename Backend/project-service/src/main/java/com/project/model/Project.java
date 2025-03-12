package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.project.constants.ProjectPriority;
import com.project.constants.ProjectStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "complex-long-id-generator")
    @GenericGenerator(name = "complex-long-id-generator", strategy = "com.project.util.ComplexLongIdGenerator")
    private Long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(length = 1000)
    private String projectDescription;

    @Enumerated(EnumType.STRING)
    private ProjectPriority projectPriority = ProjectPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus = ProjectStatus.ON_TRACK;

    @Column
    private Boolean isActive = true;

    private Long projectManagerId;

    @ElementCollection
    private List<Long> teamsInvolvedId;

    @ElementCollection
    private List<Long> objectiveId;

    @Transient
    private List<Objective> objectives;

    @ElementCollection
    private List<Long> keyResultIds;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date projectCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date projectDueDate;

    @Transient
    private Double projectProgress;
}

