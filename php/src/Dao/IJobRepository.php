<?php

namespace App\Dao;

use App\Domain\IJob;

interface IJobRepository {
    public function createJob(IJob $job): int;
}
