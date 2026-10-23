<?php

namespace App\Service;

class ScheduleService {

    public function maxNonOverlapping(array $jobs): array {
        $intervals = [];
        foreach ($jobs as $job) {
            $intervals[] = [
                'name' => $job['name'],
                'start_hour' => (int)$job['start_hour'],
                'end_hour' => (int)$job['end_hour'],
            ];
        }

        $n = count($intervals);
        for ($i = 0; $i < $n; $i++) {
            for ($j = 0; $j < $n - 1; $j++) {
                if ($intervals[$j]['start_hour'] > $intervals[$j + 1]['start_hour']) {
                    $tmp = $intervals[$j];
                    $intervals[$j] = $intervals[$j + 1];
                    $intervals[$j + 1] = $tmp;
                }
            }
        }

        $selected = [];
        $lastEnd = 0;
        foreach ($intervals as $interval) {
            if ($interval['start_hour'] > $lastEnd) {
                $selected[] = $interval;
                $lastEnd = $interval['end_hour'];
            }
        }

        return $selected;
    }
}
