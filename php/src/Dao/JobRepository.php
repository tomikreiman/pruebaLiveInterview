<?php

namespace App\Dao;

use App\Domain\IJob;
use PDO;
use PDOException;

class JobRepository implements IJobRepository {
    private PDO $pdo;

    public function __construct(PDO $pdo) {
        $this->pdo = $pdo;
    }

    public function createJob(IJob $job): int {
        $stmt = $this->pdo->prepare("
            INSERT INTO jobs (name, tags, salary)
            VALUES (:name, :tags, :salary)
        ");

        $stmt->execute([
            ':name' => $job->getName(),
            ':tags' => json_encode($job->getTags()),
            ':salary' => $job->getSalary()
        ]);

        return (int)$this->pdo->lastInsertId();
    }

    public function getAll(): array {
        $stmt = $this->pdo->query("SELECT id, name, tags, salary, start_hour, end_hour, created_at FROM jobs");

        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
}
