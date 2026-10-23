package com.avature.jobnotifier.service;

import com.avature.jobnotifier.dao.IPersonRepository;
import com.avature.jobnotifier.domain.IJob;
import com.avature.jobnotifier.domain.IPerson;

import java.sql.SQLException;

public class JobNotifier {
    private final IPersonRepository personRepository;
    private final IMailSender mailSender;
    private final ISmsSender smsSender;

    public JobNotifier(IPersonRepository personRepository, IMailSender mailSender, ISmsSender smsSender) {
        this.personRepository = personRepository;
        this.mailSender = mailSender;
        this.smsSender = smsSender;
    }

    public void notify(IJob job) throws SQLException {
        for (IPerson person : personRepository.getAll()) {
            if (!job.getTags().contains(person.getPersonalTag())) {
                continue;
            }
            notifyPerson(person, job);
        }
    }

    private void notifyPerson(IPerson person, IJob job) {
        if (person.getPhone() != null) {
            smsSender.send(person.getPhone(), getMessage(job));
        }
        if (person.getMail() != null) {
            mailSender.send(person.getMail(), getMessage(job));
        }
    }

    private String getMessage(IJob job) {
        String salary;
        if (job.getSalary() > 100000) {
            salary = 100000 + "+";
        } else if (job.getSalary() < 30000) {
            salary = 30000 + "-";
        } else {
            salary = String.valueOf(job.getSalary());
        }

        return "There is a new alert for your subscription. Job "
                + job.getName()
                + ", with salary "
                + salary
                + ".";
    }
}
