package com.avature.jobnotifier.controller;

import com.avature.jobnotifier.domain.Job;
import com.avature.jobnotifier.service.JobNotifier;
import com.avature.jobnotifier.service.JobService;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JobController {
    private final JobService jobService;
    private final JobNotifier jobNotifier;

    public JobController(JobService jobService, JobNotifier jobNotifier) {
        this.jobService = jobService;
        this.jobNotifier = jobNotifier;
    }

    @SuppressWarnings("unchecked")
    public void createJob(Context ctx) {
        Map<String, Object> data;
        try {
            data = ctx.bodyAsClass(Map.class);
        } catch (Exception e) {
            data = Map.of();
        }

        try {
            if (!data.containsKey("name")) {
                throw new IllegalArgumentException("Name is required");
            }

            String name = String.valueOf(data.get("name"));
            List<String> tags = (List<String>) data.getOrDefault("tags", List.of());
            int salary = data.get("salary") == null ? 0 : ((Number) data.get("salary")).intValue();

            Job job = new Job(name, tags, salary);

            int jobId = jobService.createJob(job);

            jobNotifier.notify(job);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("success", true);
            body.put("job_id", jobId);
            body.put("message", "Job created successfully");

            ctx.status(201).json(body);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        } catch (SQLException e) {
            ctx.status(500).json(Map.of("error", "Failed to create job: " + e.getMessage()));
        }
    }
}
