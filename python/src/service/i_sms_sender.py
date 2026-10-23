from abc import ABC, abstractmethod


class ISmsSender(ABC):
    @abstractmethod
    def send(self, phone: str, message: str) -> None:
        """Raises SmsCannotBeSentException"""
        pass
