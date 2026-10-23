package com.avature.jobnotifier.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScheduleService {

    public List<Map<String, Object>> maxNonOverlapping(List<Map<String, Object>> jobs) {
        List<Map<String, Object>> intervals = new ArrayList<>();
        for (Map<String, Object> job : jobs) {
            Map<String, Object> interval = new LinkedHashMap<>();
            interval.put("name", job.get("name"));
            interval.put("start_hour", ((Number) job.get("start_hour")).intValue());
            interval.put("end_hour", ((Number) job.get("end_hour")).intValue());
            intervals.add(interval);
        }

        int n = intervals.size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (startHour(intervals.get(j)) > startHour(intervals.get(j + 1))) {
                    Map<String, Object> tmp = intervals.get(j);
                    intervals.set(j, intervals.get(j + 1));
                    intervals.set(j + 1, tmp);
                }
            }
        }

        List<Map<String, Object>> selected = new ArrayList<>();
        int lastEnd = 0;
        for (Map<String, Object> interval : intervals) {
            if (startHour(interval) > lastEnd) {
                selected.add(interval);
                lastEnd = endHour(interval);
            }
        }

        return selected;
    }

    private static int startHour(Map<String, Object> interval) {
        return (int) interval.get("start_hour");
    }

    private static int endHour(Map<String, Object> interval) {
        return (int) interval.get("end_hour");
    }
}
