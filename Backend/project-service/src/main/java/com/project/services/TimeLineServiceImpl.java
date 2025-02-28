package com.project.services;

import com.netflix.discovery.converters.Auto;
import com.project.model.TimeLine;
import com.project.repository.TimeLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TimeLineServiceImpl implements TimeLineService{

    @Autowired
    private TimeLineRepository timeLineRepository;


    @Override
    public TimeLine createTimeLine(TimeLine timeLine) {
        return timeLineRepository.save(timeLine);
    }

    @Override
    public List<TimeLine> getAllTimeLine() {
        return timeLineRepository.findAll();
    }

    @Override
    public TimeLine getTimeLineById(Long timeLineId) {
        return timeLineRepository.findById(timeLineId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public TimeLine updateTimeLine(Long timeLineId, TimeLine timeLine) {
        TimeLine existingTimeLine = timeLineRepository.findById(timeLineId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if(existingTimeLine != null){
            existingTimeLine.setTimeLineHeading(timeLine.getTimeLineHeading());
            existingTimeLine.setTimeLineStatus(timeLine.getTimeLineStatus());
        }

        return existingTimeLine;
    }

    @Override
    public void removeTimeLine(Long timeLineId) {
        timeLineRepository.deleteById(timeLineId);
    }

    @Override
    public List<TimeLine> getAllTimeLine(Long projectId) {
        List<TimeLine> getAllTimeLine = timeLineRepository.findAllByTimeLineAssociatedProject(projectId);
        return getAllTimeLine;
    }
}
