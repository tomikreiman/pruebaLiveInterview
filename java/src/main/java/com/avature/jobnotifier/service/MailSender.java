package com.avature.jobnotifier.service;

import org.slf4j.Logger;

public class MailSender implements IMailSender {
    private final Logger logger;

    public MailSender(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void send(String mail, String message) {
        logger.info("Sending email to: {} [message={}]", mail, message);
    }
}
