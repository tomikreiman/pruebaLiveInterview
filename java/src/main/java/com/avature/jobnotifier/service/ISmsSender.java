package com.avature.jobnotifier.service;

public interface ISmsSender {
    /** May throw SmsCannotBeSentException */
    void send(String phone, String message);
}
