package com.avature.jobnotifier.controller;

import com.avature.jobnotifier.dao.IJobRepository;
import com.avature.jobnotifier.service.SalaryStatsService;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StatsController {
    private final IJobRepository jobRepository;
    private final SalaryStatsService statsService;

    public StatsController(IJobRepository jobRepository, SalaryStatsService statsService) {
        this.jobRepository = jobRepository;
        this.statsService = statsService;
    }

    public void getStats(Context ctx) throws SQLException {
        List<Map<String, Object>> jobs = jobRepository.getAll();
        Map<String, Object> stats = statsService.computeStats(jobs);

        ctx.json(stats);
    }
}
