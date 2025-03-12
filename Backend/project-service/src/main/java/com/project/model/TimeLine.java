package com.project.model;

import com.project.constants.TimeLineStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TimeLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "complex-long-id-generator")
    @GenericGenerator(name = "complex-long-id-generator", strategy = "com.project.util.ComplexLongIdGenerator")
    private Long timeLineId;
    private String timeLineHeading;
    private Long timeLineAssociatedProject;
    private TimeLineStatus timeLineStatus;
}
