package com.avature.jobnotifier.scripts;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class InitDb {
    private InitDb() {
    }

    public static void main(String[] args) throws SQLException {
        String dbPath = System.getenv().getOrDefault("DB_PATH", "data/jobs.db");
        run(dbPath);
    }

    public static void run(String dbPath) throws SQLException {
        File dbFile = new File(dbPath);
        File parent = dbFile.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("""
                        CREATE TABLE IF NOT EXISTS jobs (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            tags TEXT,
                            salary INTEGER,
                            start_hour INTEGER,
                            end_hour INTEGER,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                        )
                        """);

                stmt.execute("""
                        CREATE TABLE IF NOT EXISTS people (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            personal_tag TEXT NOT NULL,
                            mail TEXT,
                            phone TEXT,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                        )
                        """);
            }

            seedJobsIfEmpty(connection);
            seedPeopleIfEmpty(connection);

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    private static void seedJobsIfEmpty(Connection connection) throws SQLException {
        int count = 0;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM jobs")) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        if (count != 0) {
            return;
        }

        Object[][] samples = {
                {"Alpha", "[\"php\"]", 50000, 9, 10},
                {"Bravo", "[\"go\"]", 60000, 10, 11},
                {"Charlie", "[\"php\",\"backend\"]", 70000, 11, 12},
                {"Delta", "[\"remote\"]", 80000, 13, 14},
                {"Echo", "[\"php\"]", 90000, 8, 17},
        };

        String sql = "INSERT INTO jobs (name, tags, salary, start_hour, end_hour) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Object[] sample : samples) {
                stmt.setString(1, (String) sample[0]);
                stmt.setString(2, (String) sample[1]);
                stmt.setInt(3, (Integer) sample[2]);
                stmt.setInt(4, (Integer) sample[3]);
                stmt.setInt(5, (Integer) sample[4]);
                stmt.executeUpdate();
            }
        }
    }

    private static void seedPeopleIfEmpty(Connection connection) throws SQLException {
        int count = 0;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM people")) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        if (count != 0) {
            return;
        }

        Object[][] samples = {
                {"Alice", "php", "alice@example.com", "123-456-7890"},
                {"Bob", "go", "bob@example.com", "234-567-8901"},
                {"Charlie", "remote", "charlie@example.com", "345-678-9012"},
        };

        String sql = "INSERT INTO people (name, personal_tag, mail, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Object[] sample : samples) {
                stmt.setString(1, (String) sample[0]);
                stmt.setString(2, (String) sample[1]);
                stmt.setString(3, (String) sample[2]);
                stmt.setString(4, (String) sample[3]);
                stmt.executeUpdate();
            }
        }
    }
}
