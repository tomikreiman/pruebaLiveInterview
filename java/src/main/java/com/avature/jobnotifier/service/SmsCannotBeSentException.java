package com.avature.jobnotifier.service;

public class SmsCannotBeSentException extends RuntimeException {
    public SmsCannotBeSentException(String message) {
        super(message);
    }

    public SmsCannotBeSentException(String message, Throwable cause) {
        super(message, cause);
    }
}
