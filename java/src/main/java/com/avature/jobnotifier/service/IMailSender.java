package com.avature.jobnotifier.service;

public interface IMailSender {
    /** May throw MailCannotBeSentException */
    void send(String mail, String message);
}
