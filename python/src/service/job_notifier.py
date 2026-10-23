from src.dao.i_person_repository import IPersonRepository
from src.domain.i_job import IJob
from src.domain.i_person import IPerson
from src.service.i_mail_sender import IMailSender
from src.service.i_sms_sender import ISmsSender


class JobNotifier:
    def __init__(
        self,
        person_repository: IPersonRepository,
        mail_sender: IMailSender,
        sms_sender: ISmsSender,
    ):
        self._person_repository = person_repository
        self._mail_sender = mail_sender
        self._sms_sender = sms_sender

    def notify(self, job: IJob) -> None:
        for person in self._person_repository.get_all():
            if person.get_personal_tag() not in job.get_tags():
                continue
            self._notify_person(person, job)

    def _notify_person(self, person: IPerson, job: IJob) -> None:
        if person.get_phone():
            self._sms_sender.send(person.get_phone(), self._get_message(job))
        if person.get_mail():
            self._mail_sender.send(person.get_mail(), self._get_message(job))

    def _get_message(self, job: IJob) -> str:
        if job.get_salary() > 100000:
            salary = str(100000) + "+"
        elif job.get_salary() < 30000:
            salary = str(30000) + "-"
        else:
            salary = job.get_salary()

        return (
            "There is a new alert for your subscription. Job "
            + job.get_name()
            + ", with salary "
            + str(salary)
            + "."
        )
