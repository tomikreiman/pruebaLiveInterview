from abc import ABC, abstractmethod


class IMailSender(ABC):
    @abstractmethod
    def send(self, mail: str, message: str) -> None:
        """Raises MailCannotBeSentException"""
        pass
