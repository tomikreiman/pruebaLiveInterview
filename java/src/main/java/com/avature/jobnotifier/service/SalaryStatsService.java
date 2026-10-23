package com.avature.jobnotifier.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalaryStatsService {

    public Map<String, Object> computeStats(List<Map<String, Object>> jobs) {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total_jobs", jobs.size());
        stats.put("average_salary", averageSalary(jobs));
        stats.put("median_salary", medianSalary(jobs));
        stats.put("count_by_tag", countByTag(jobs));
        stats.put("salary_bands", salaryBands(jobs));
        return stats;
    }

    private int averageSalary(List<Map<String, Object>> jobs) {
        int sum = 0;
        for (Map<String, Object> job : jobs) {
            sum += salaryOf(job);
        }

        return sum / jobs.size();
    }

    private int medianSalary(List<Map<String, Object>> jobs) {
        List<Integer> salaries = new ArrayList<>();
        for (Map<String, Object> job : jobs) {
            salaries.add(salaryOf(job));
        }

        int n = salaries.size();
        for (int i = 0; i < n; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (salaries.get(j) < salaries.get(minIndex)) {
                    minIndex = j;
                }
            }
            int tmp = salaries.get(i);
            salaries.set(i, salaries.get(minIndex));
            salaries.set(minIndex, tmp);
        }

        return salaries.get(n / 2);
    }

    private Map<Object, Integer> countByTag(List<Map<String, Object>> jobs) {
        Map<Object, Integer> counts = new LinkedHashMap<>();
        for (Map<String, Object> job : jobs) {
            Object tag = job.get("tags");
            counts.merge(tag, 1, Integer::sum);
        }

        return counts;
    }

    private List<Map<String, Object>> salaryBands(List<Map<String, Object>> jobs) {
        int low = 0;
        int mid = 0;
        int high = 0;
        for (Map<String, Object> job : jobs) {
            int salary = salaryOf(job);
            if (salary >= 0 && salary <= 50000) {
                low++;
            }
            if (salary >= 50000 && salary <= 100000) {
                mid++;
            }
            if (salary >= 100000) {
                high++;
            }
        }

        List<Map<String, Object>> bands = new ArrayList<>();
        bands.add(band("0-50000", low));
        bands.add(band("50000-100000", mid));
        bands.add(band("100000+", high));
        return bands;
    }

    private static Map<String, Object> band(String range, int count) {
        Map<String, Object> band = new LinkedHashMap<>();
        band.put("range", range);
        band.put("count", count);
        return band;
    }

    private static int salaryOf(Map<String, Object> job) {
        return ((Number) job.get("salary")).intValue();
    }
}
