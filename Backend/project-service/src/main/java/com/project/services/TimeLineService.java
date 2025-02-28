package com.project.services;

import com.project.model.TimeLine;

import java.util.List;

public interface TimeLineService {
    public TimeLine createTimeLine(TimeLine timeLine);
    public List<TimeLine> getAllTimeLine();
    public TimeLine getTimeLineById(Long timeLineId);
    public TimeLine updateTimeLine(Long timeLineId, TimeLine timeLine);
    public void removeTimeLine(Long timeLineId);
    public List<TimeLine> getAllTimeLine(Long projectId);
}
