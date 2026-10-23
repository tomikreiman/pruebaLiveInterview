from abc import ABC, abstractmethod


class IJob(ABC):
    @abstractmethod
    def get_name(self) -> str:
        pass

    @abstractmethod
    def get_tags(self) -> list[str]:
        pass

    @abstractmethod
    def get_salary(self) -> int:
        pass
