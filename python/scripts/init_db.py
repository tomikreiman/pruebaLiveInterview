import json
import os
import sqlite3

import sys

# Add the project root to the Python path so absolute imports work
project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
if project_root not in sys.path:
    sys.path.insert(0, project_root)


def main() -> None:
    db_path = os.environ.get("DB_PATH", os.path.join(project_root, "data", "jobs.db"))
    os.makedirs(os.path.dirname(db_path), exist_ok=True)

    try:
        db = sqlite3.connect(db_path)
        db.row_factory = sqlite3.Row

        # Create jobs table if it doesn't exist
        db.execute(
            """
            CREATE TABLE IF NOT EXISTS jobs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                tags TEXT,
                salary INTEGER,
                start_hour INTEGER,
                end_hour INTEGER,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """
        )

        # Create people table if it doesn't exist
        db.execute(
            """
            CREATE TABLE IF NOT EXISTS people (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                personal_tag TEXT NOT NULL,
                mail TEXT,
                phone TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """
        )

        # Seed sample jobs with interview/event slots [start_hour, end_hour) in 24h format
        # if the table is empty. Slots occupy a single shared resource (one room/interviewer).
        count = db.execute("SELECT COUNT(*) FROM jobs").fetchone()[0]
        if count == 0:
            samples = [
                ["Alpha", ["php"], 50000, 9, 10],
                ["Bravo", ["go"], 60000, 10, 11],
                ["Charlie", ["php", "backend"], 70000, 11, 12],
                ["Delta", ["remote"], 80000, 13, 14],
                ["Echo", ["php"], 90000, 8, 17],
            ]
            for name, tags, salary, start, end in samples:
                db.execute(
                    """
                    INSERT INTO jobs (name, tags, salary, start_hour, end_hour)
                    VALUES (?, ?, ?, ?, ?)
                    """,
                    (name, json.dumps(tags), salary, start, end),
                )

        # Seed sample people if the table is empty
        people_count = db.execute("SELECT COUNT(*) FROM people").fetchone()[0]
        if people_count == 0:
            people_samples = [
                ["Alice", "php", "alice@example.com", "123-456-7890"],
                ["Bob", "go", "bob@example.com", "234-567-8901"],
                ["Charlie", "remote", "charlie@example.com", "345-678-9012"],
            ]
            for name, personal_tag, mail, phone in people_samples:
                db.execute(
                    """
                    INSERT INTO people (name, personal_tag, mail, phone)
                    VALUES (?, ?, ?, ?)
                    """,
                    (name, personal_tag, mail, phone),
                )

        db.commit()
        print("Database initialized successfully.")
    except sqlite3.Error as e:
        print(f"Database connection failed: {e}")
        raise


if __name__ == "__main__":
    main()
