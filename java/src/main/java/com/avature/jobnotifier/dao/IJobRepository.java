package com.avature.jobnotifier.dao;

import com.avature.jobnotifier.domain.IJob;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IJobRepository {
    int createJob(IJob job) throws SQLException;

    List<Map<String, Object>> getAll() throws SQLException;
}
