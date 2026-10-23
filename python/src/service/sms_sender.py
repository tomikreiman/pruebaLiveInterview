import logging

from src.service.i_sms_sender import ISmsSender
from src.service.sms_cannot_be_sent_exception import SmsCannotBeSentException


class SmsSender(ISmsSender):
    def __init__(self, logger: logging.Logger):
        self._logger = logger

    def send(self, phone: str, message: str) -> None:
        """Raises SmsCannotBeSentException"""
        self._logger.info("Sending SMS to: %s", phone, extra={"notification_message": message})
