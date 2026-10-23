package com.avature.jobnotifier.domain;

import java.util.List;

public interface IJob {
    String getName();
    List<String> getTags();
    int getSalary();
}
