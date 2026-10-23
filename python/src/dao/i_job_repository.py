from abc import ABC, abstractmethod

from ..domain.i_job import IJob


class IJobRepository(ABC):
    @abstractmethod
    def create_job(self, job: IJob) -> int:
        pass
