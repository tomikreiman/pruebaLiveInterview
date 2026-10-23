package com.avature.jobnotifier.config;

import com.avature.jobnotifier.controller.HomeController;
import com.avature.jobnotifier.controller.JobController;
import com.avature.jobnotifier.dao.IJobRepository;
import com.avature.jobnotifier.dao.IPersonRepository;
import com.avature.jobnotifier.dao.JobRepository;
import com.avature.jobnotifier.dao.PersonRepository;
import com.avature.jobnotifier.service.IMailSender;
import com.avature.jobnotifier.service.ISmsSender;
import com.avature.jobnotifier.service.JobNotifier;
import com.avature.jobnotifier.service.JobService;
import com.avature.jobnotifier.service.MailSender;
import com.avature.jobnotifier.service.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Dependencies {
    private Dependencies() {
    }

    public static void configure(Container container) {
        container.set("logger", c -> LoggerFactory.getLogger("job-notifier"));

        container.set("connection", c -> createConnection());

        container.set("mail_sender", c -> new MailSender(c.<Logger>get("logger")));
        container.set("sms_sender", c -> new SmsSender(c.<Logger>get("logger")));

        container.set("job_repository", c -> new JobRepository(c.<Connection>get("connection")));
        container.set("person_repository", c -> new PersonRepository(c.<Connection>get("connection")));

        container.set("job_notifier", c -> new JobNotifier(
                c.<IPersonRepository>get("person_repository"),
                c.<IMailSender>get("mail_sender"),
                c.<ISmsSender>get("sms_sender")
        ));

        container.set("job_service", c -> new JobService(c.<IJobRepository>get("job_repository")));

        container.set("job_controller", c -> new JobController(
                c.<JobService>get("job_service"),
                c.<JobNotifier>get("job_notifier")
        ));

        container.set("home_controller", c -> new HomeController());
    }

    private static Connection createConnection() {
        String dbPath = System.getenv().getOrDefault("DB_PATH", "data/jobs.db");
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database at " + dbPath, e);
        }
    }
}
