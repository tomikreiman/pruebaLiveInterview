import logging

from src.service.i_mail_sender import IMailSender
from src.service.mail_cannot_be_sent_exception import MailCannotBeSentException


class MailSender(IMailSender):
    def __init__(self, logger: logging.Logger):
        self._logger = logger

    def send(self, mail: str, message: str) -> None:
        """Raises MailCannotBeSentException"""
        self._logger.info("Sending email to: %s", mail, extra={"notification_message": message})
