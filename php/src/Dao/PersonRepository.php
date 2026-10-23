<?php

namespace App\Dao;

use App\Domain\Person;
use PDO;

class PersonRepository implements IPersonRepository {
    private PDO $pdo;

    public function __construct(PDO $pdo) {
        $this->pdo = $pdo;
    }

    public function getAll(): array {
        $stmt = $this->pdo->query("SELECT id, name, personal_tag, mail, phone FROM people");
        $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

        $people = [];
        foreach ($rows as $row) {
            $people[] = new Person(
                $row['name'],
                $row['personal_tag'],
                $row['mail'],
                $row['phone']
            );
        }

        return $people;
    }
}
