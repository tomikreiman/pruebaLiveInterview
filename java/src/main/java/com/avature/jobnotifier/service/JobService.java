package com.avature.jobnotifier.service;

import com.avature.jobnotifier.dao.IJobRepository;
import com.avature.jobnotifier.domain.IJob;

import java.sql.SQLException;

public class JobService {
    private final IJobRepository jobRepository;

    public JobService(IJobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public int createJob(IJob job) throws SQLException {
        if (job.getName() == null || job.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        return jobRepository.createJob(job);
    }
}
