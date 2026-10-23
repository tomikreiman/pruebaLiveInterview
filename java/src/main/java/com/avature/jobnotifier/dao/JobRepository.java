package com.avature.jobnotifier.dao;

import com.avature.jobnotifier.domain.IJob;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Map<String, Object>> getAll() throws SQLException {
        String sql = "SELECT id, name, tags, salary, start_hour, end_hour, created_at FROM jobs";
        List<Map<String, Object>> jobs = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rows = stmt.executeQuery(sql)) {
            ResultSetMetaData meta = rows.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rows.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnLabel(i), rows.getObject(i));
                }
                jobs.add(row);
            }
        }

        return jobs;
    }

    private static String toJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
