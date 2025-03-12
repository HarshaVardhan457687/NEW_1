package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Objective {
    private Long objectiveId;
    private String objectiveName;
    private String objectiveDescription;
    private Long mappedProject;
    @ElementCollection
    private List<Long> assignedTo;
    @ElementCollection
    private List<Long> keyResultIds;
    @Transient
    private List<KeyResult> keyResult;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date objectiveStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date objectiveDueDate;
    private String objectiveStatus;
    private boolean objectiveIsActive;

}