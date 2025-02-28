package com.project.model;

import com.project.constants.TimeLineStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TimeLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeLineId;
    private String timeLineHeading;
    private Long timeLineAssociatedProject;
    private TimeLineStatus timeLineStatus;

    public Long getTimeLineId() {
        return timeLineId;
    }

    public void setTimeLineId(Long timeLineId) {
        this.timeLineId = timeLineId;
    }

    public String getTimeLineHeading() {
        return timeLineHeading;
    }

    public void setTimeLineHeading(String timeLineHeading) {
        this.timeLineHeading = timeLineHeading;
    }

    public Long getTimeLineAssociatedProject() {
        return timeLineAssociatedProject;
    }

    public void setTimeLineAssociatedProject(Long timeLineAssociatedProject) {
        this.timeLineAssociatedProject = timeLineAssociatedProject;
    }

    public TimeLineStatus getTimeLineStatus() {
        return timeLineStatus;
    }

    public void setTimeLineStatus(TimeLineStatus timeLineStatus) {
        this.timeLineStatus = timeLineStatus;
    }
}
