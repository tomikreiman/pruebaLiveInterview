package com.avature.jobnotifier.domain;

public class Person implements IPerson {
    private final String name;
    private final String personalTag;
    private final String mail;
    private final String phone;

    public Person(String name, String personalTag, String mail, String phone) {
        this.name = name;
        this.personalTag = personalTag;
        this.mail = mail;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPersonalTag() {
        return personalTag;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getMail() {
        return mail;
    }
}
