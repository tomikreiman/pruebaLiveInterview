<?php

require __DIR__ . '/../vendor/autoload.php';

$dbPath = getenv('DB_PATH') ?: __DIR__ . '/../data/jobs.db';
$dsn = 'sqlite:' . $dbPath;

try {
    $pdo = new PDO($dsn);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    // Create jobs table if it doesn't exist
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS jobs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            tags TEXT,
            salary INTEGER,
            start_hour INTEGER,
            end_hour INTEGER,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    ");

    // Create people table if it doesn't exist
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS people (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            personal_tag TEXT NOT NULL,
            mail TEXT,
            phone TEXT,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    ");

    // Seed sample jobs with interview/event slots [start_hour, end_hour) in 24h format
    // if the table is empty. Slots occupy a single shared resource (one room/interviewer).
    $count = (int)$pdo->query("SELECT COUNT(*) FROM jobs")->fetchColumn();
    if ($count === 0) {
        $samples = [
            ['Alpha',   ['php'],             50000,  9, 10],
            ['Bravo',   ['go'],              60000, 10, 11],
            ['Charlie', ['php', 'backend'],  70000, 11, 12],
            ['Delta',   ['remote'],          80000, 13, 14],
            ['Echo',    ['php'],             90000,  8, 17],
        ];
        $stmt = $pdo->prepare("
            INSERT INTO jobs (name, tags, salary, start_hour, end_hour)
            VALUES (:name, :tags, :salary, :start_hour, :end_hour)
        ");
        foreach ($samples as [$name, $tags, $salary, $start, $end]) {
            $stmt->execute([
                ':name' => $name,
                ':tags' => json_encode($tags),
                ':salary' => $salary,
                ':start_hour' => $start,
                ':end_hour' => $end,
            ]);
        }
    }

    // Seed sample people if the table is empty
    $peopleCount = (int)$pdo->query("SELECT COUNT(*) FROM people")->fetchColumn();
    if ($peopleCount === 0) {
        $peopleSamples = [
            ['Alice', 'php', 'alice@example.com', '123-456-7890'],
            ['Bob', 'go', 'bob@example.com', '234-567-8901'],
            ['Charlie', 'remote', 'charlie@example.com', '345-678-9012'],
        ];
        $stmt = $pdo->prepare("
            INSERT INTO people (name, personal_tag, mail, phone)
            VALUES (:name, :personal_tag, :mail, :phone)
        ");
        foreach ($peopleSamples as [$name, $personal_tag, $mail, $phone]) {
            $stmt->execute([
                ':name' => $name,
                ':personal_tag' => $personal_tag,
                ':mail' => $mail,
                ':phone' => $phone,
            ]);
        }
    }
    
    echo "Database initialized successfully.\n";
} catch (PDOException $e) {
    die("Database connection failed: " . $e->getMessage() . "\n");
}
