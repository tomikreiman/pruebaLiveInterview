package com.avature.jobnotifier.service;

public class MailCannotBeSentException extends RuntimeException {
    public MailCannotBeSentException(String message) {
        super(message);
    }

    public MailCannotBeSentException(String message, Throwable cause) {
        super(message, cause);
    }
}
