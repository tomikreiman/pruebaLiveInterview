import json

from flask import Response

from src.dao.i_job_repository import IJobRepository
from src.service.salary_stats_service import SalaryStatsService


class StatsController:
    def __init__(self, job_repository: IJobRepository, stats_service: SalaryStatsService):
        self._job_repository = job_repository
        self._stats_service = stats_service

    def get_stats(self) -> Response:
        jobs = self._job_repository.get_all()
        stats = self._stats_service.compute_stats(jobs)

        return Response(
            json.dumps(stats),
            status=200,
            mimetype="application/json",
        )
