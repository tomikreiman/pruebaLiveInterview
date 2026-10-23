from .i_job import IJob


class Job(IJob):
    def __init__(self, name: str, tags: list[str], salary: int):
        self._name = name
        self._tags = tags
        self._salary = salary

    def get_tags(self) -> list[str]:
        return self._tags

    def get_name(self) -> str:
        return self._name

    def get_salary(self) -> int:
        return self._salary
