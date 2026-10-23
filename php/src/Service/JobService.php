<?php

namespace App\Service;

use App\Dao\IJobRepository;
use App\Domain\IJob;
use InvalidArgumentException;
use PDOException;

class JobService {
    private IJobRepository $jobRepository;

    public function __construct(IJobRepository $jobRepository) {
        $this->jobRepository = $jobRepository;
    }

    public function createJob(IJob $job): int {
        if (empty($job->getName())) {
            throw new InvalidArgumentException('Name is required');
        }

        return $this->jobRepository->createJob($job);
    }
}
