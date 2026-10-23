package com.avature.jobnotifier.dao;

import com.avature.jobnotifier.domain.IJob;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JobRepository implements IJobRepository {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Connection connection;

    public JobRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int createJob(IJob job) throws SQLException {
        String sql = "INSERT INTO jobs (name, tags, salary) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, job.getName());
            stmt.setString(2, toJson(job.getTags()));
            stmt.setInt(3, job.getSalary());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                return 0;
            }
        }
    }

    private static String toJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
