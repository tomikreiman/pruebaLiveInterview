from abc import ABC, abstractmethod

from ..domain.person import Person


class IPersonRepository(ABC):
    @abstractmethod
    def get_all(self) -> list[Person]:
        pass
