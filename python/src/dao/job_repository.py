import json
import sqlite3

from src.domain.i_job import IJob
from src.dao.i_job_repository import IJobRepository


class JobRepository(IJobRepository):
    def __init__(self, db: sqlite3.Connection):
        self._db = db

    def create_job(self, job: IJob) -> int:
        cursor = self._db.execute(
            """
            INSERT INTO jobs (name, tags, salary)
            VALUES (?, ?, ?)
            """,
            (job.get_name(), json.dumps(job.get_tags()), job.get_salary()),
        )
        self._db.commit()
        return cursor.lastrowid
