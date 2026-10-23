from src.dao.i_job_repository import IJobRepository
from src.domain.i_job import IJob


class JobService:
    def __init__(self, job_repository: IJobRepository):
        self._job_repository = job_repository

    def create_job(self, job: IJob) -> int:
        if not job.get_name():
            raise ValueError("Name is required")

        return self._job_repository.create_job(job)
