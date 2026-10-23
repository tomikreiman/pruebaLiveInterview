<?php

namespace App\Service;

class SalaryStatsService {

    public function computeStats(array $jobs): array {
        return [
            'total_jobs' => count($jobs),
            'average_salary' => $this->averageSalary($jobs),
            'median_salary' => $this->medianSalary($jobs),
            'count_by_tag' => $this->countByTag($jobs),
            'salary_bands' => $this->salaryBands($jobs),
        ];
    }

    private function averageSalary(array $jobs): int {
        $sum = 0;
        foreach ($jobs as $job) {
            $sum += $job['salary'];
        }

        return (int)($sum / count($jobs));
    }

    private function medianSalary(array $jobs): int {
        $salaries = [];
        foreach ($jobs as $job) {
            $salaries[] = $job['salary'];
        }

        $n = count($salaries);
        for ($i = 0; $i < $n; $i++) {
            $minIndex = $i;
            for ($j = $i + 1; $j < $n; $j++) {
                if ($salaries[$j] < $salaries[$minIndex]) {
                    $minIndex = $j;
                }
            }
            $tmp = $salaries[$i];
            $salaries[$i] = $salaries[$minIndex];
            $salaries[$minIndex] = $tmp;
        }

        return (int)$salaries[intdiv($n, 2)];
    }

    private function countByTag(array $jobs): array {
        $counts = [];
        foreach ($jobs as $job) {
            $tag = $job['tags'];
            if (!isset($counts[$tag])) {
                $counts[$tag] = 0;
            }
            $counts[$tag]++;
        }

        return $counts;
    }

    private function salaryBands(array $jobs): array {
        $low = 0;
        $mid = 0;
        $high = 0;
        foreach ($jobs as $job) {
            $salary = $job['salary'];
            if ($salary >= 0 && $salary <= 50000) {
                $low++;
            }
            if ($salary >= 50000 && $salary <= 100000) {
                $mid++;
            }
            if ($salary >= 100000) {
                $high++;
            }
        }

        return [
            ['range' => '0-50000', 'count' => $low],
            ['range' => '50000-100000', 'count' => $mid],
            ['range' => '100000+', 'count' => $high],
        ];
    }
}
