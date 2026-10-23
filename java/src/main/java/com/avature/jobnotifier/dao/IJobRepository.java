package com.avature.jobnotifier.dao;

import com.avature.jobnotifier.domain.IJob;

import java.sql.SQLException;

public interface IJobRepository {
    int createJob(IJob job) throws SQLException;
}
