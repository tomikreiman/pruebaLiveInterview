import json

from flask import Response

from src.dao.i_job_repository import IJobRepository
from src.service.schedule_service import ScheduleService


class ScheduleController:
    def __init__(self, job_repository: IJobRepository, schedule_service: ScheduleService):
        self._job_repository = job_repository
        self._schedule_service = schedule_service

    def get_schedule(self) -> Response:
        jobs = self._job_repository.get_all()
        selected = self._schedule_service.max_non_overlapping(jobs)

        payload = {
            "selected_count": len(selected),
            "selected": selected,
        }

        return Response(
            json.dumps(payload),
            status=200,
            mimetype="application/json",
        )
