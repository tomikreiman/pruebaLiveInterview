package com.avature.jobnotifier.controller;

import com.avature.jobnotifier.dao.IJobRepository;
import com.avature.jobnotifier.service.ScheduleService;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ScheduleController {
    private final IJobRepository jobRepository;
    private final ScheduleService scheduleService;

    public ScheduleController(IJobRepository jobRepository, ScheduleService scheduleService) {
        this.jobRepository = jobRepository;
        this.scheduleService = scheduleService;
    }

    public void getSchedule(Context ctx) throws SQLException {
        List<Map<String, Object>> jobs = jobRepository.getAll();
        List<Map<String, Object>> selected = scheduleService.maxNonOverlapping(jobs);

        ctx.json(Map.of(
                "selected_count", selected.size(),
                "selected", selected
        ));
    }
}
