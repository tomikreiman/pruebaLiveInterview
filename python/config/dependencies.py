import logging
import os
import sqlite3

from src.dao.job_repository import JobRepository
from src.dao.person_repository import PersonRepository
from src.service.job_notifier import JobNotifier
from src.service.job_service import JobService
from src.service.mail_sender import MailSender
from src.service.sms_sender import SmsSender
from src.service.salary_stats_service import SalaryStatsService
from src.service.schedule_service import ScheduleService
from src.controller.home_controller import HomeController
from src.controller.job_controller import JobController
from src.controller.stats_controller import StatsController
from src.controller.schedule_controller import ScheduleController


class Container:
    def __init__(self):
        self._registrations = {}
        self._singletons = {}

    def set(self, key, factory):
        self._registrations[key] = factory

    def get(self, key):
        if key not in self._registrations:
            raise KeyError(f"No registration for {key}")
        if key not in self._singletons:
            self._singletons[key] = self._registrations[key](self)
        return self._singletons[key]


def configure_dependencies(container: Container) -> None:
    container.set("logger", lambda c: _create_logger())

    container.set("db", lambda c: _create_db())

    container.set("mail_sender", lambda c: MailSender(c.get("logger")))
    container.set("sms_sender", lambda c: SmsSender(c.get("logger")))

    container.set("job_repository", lambda c: JobRepository(c.get("db")))
    container.set("person_repository", lambda c: PersonRepository(c.get("db")))

    container.set("job_notifier", lambda c: JobNotifier(
        c.get("person_repository"),
        c.get("mail_sender"),
        c.get("sms_sender"),
    ))

    container.set("job_service", lambda c: JobService(c.get("job_repository")))

    container.set("job_controller", lambda c: JobController(
        c.get("job_service"),
        c.get("job_notifier"),
    ))

    container.set("home_controller", lambda c: HomeController())

    container.set("salary_stats_service", lambda c: SalaryStatsService())

    container.set("stats_controller", lambda c: StatsController(
        c.get("job_repository"),
        c.get("salary_stats_service"),
    ))

    container.set("schedule_service", lambda c: ScheduleService())

    container.set("schedule_controller", lambda c: ScheduleController(
        c.get("job_repository"),
        c.get("schedule_service"),
    ))


def _create_logger() -> logging.Logger:
    logger = logging.getLogger("job-notifier")
    logger.setLevel(logging.DEBUG)
    handler = logging.StreamHandler()
    handler.setLevel(logging.DEBUG)
    logger.addHandler(handler)
    return logger


def _create_db() -> sqlite3.Connection:
    db_path = os.environ.get("DB_PATH", os.path.join(os.path.dirname(__file__), "..", "data", "jobs.db"))
    db = sqlite3.connect(db_path, check_same_thread=False)
    db.row_factory = sqlite3.Row
    return db
