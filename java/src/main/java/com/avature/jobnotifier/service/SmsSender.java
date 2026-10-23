package com.avature.jobnotifier.service;

import org.slf4j.Logger;

public class SmsSender implements ISmsSender {
    private final Logger logger;

    public SmsSender(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void send(String phone, String message) {
        logger.info("Sending SMS to: {} [message={}]", phone, message);
    }
}
