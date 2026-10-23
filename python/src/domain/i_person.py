from abc import ABC, abstractmethod


class IPerson(ABC):
    @abstractmethod
    def get_personal_tag(self) -> str:
        pass

    @abstractmethod
    def get_phone(self) -> str | None:
        pass

    @abstractmethod
    def get_mail(self) -> str | None:
        pass
