package com.avature.jobnotifier.domain;

import java.util.List;

public class Job implements IJob {
    private final String name;
    private final List<String> tags;
    private final int salary;

    public Job(String name, List<String> tags, int salary) {
        this.name = name;
        this.tags = tags;
        this.salary = salary;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public int getSalary() {
        return salary;
    }
}
