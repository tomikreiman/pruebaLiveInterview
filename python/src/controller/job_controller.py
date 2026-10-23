import json
import sqlite3

from flask import Response, request

from src.domain.job import Job
from src.service.job_notifier import JobNotifier
from src.service.job_service import JobService


class JobController:
    def __init__(self, job_service: JobService, job_notifier: JobNotifier):
        self._job_service = job_service
        self._job_notifier = job_notifier

    def create_job(self) -> Response:
        data = request.get_json(silent=True) or {}

        try:
            if "name" not in data:
                raise ValueError("Name is required")

            job = Job(
                data["name"],
                data.get("tags", []),
                int(data.get("salary", 0)),
            )

            job_id = self._job_service.create_job(job)

            self._job_notifier.notify(job)

            return Response(
                json.dumps({
                    "success": True,
                    "job_id": job_id,
                    "message": "Job created successfully",
                }),
                status=201,
                mimetype="application/json",
            )
        except ValueError as e:
            return Response(
                json.dumps({"error": str(e)}),
                status=400,
                mimetype="application/json",
            )
        except sqlite3.Error as e:
            return Response(
                json.dumps({"error": "Failed to create job: " + str(e)}),
                status=500,
                mimetype="application/json",
            )
