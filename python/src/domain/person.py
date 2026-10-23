from .i_person import IPerson


class Person(IPerson):
    def __init__(self, name: str, personal_tag: str, mail: str | None, phone: str | None):
        self._name = name
        self._personal_tag = personal_tag
        self._mail = mail
        self._phone = phone

    def get_personal_tag(self) -> str:
        return self._personal_tag

    def get_phone(self) -> str | None:
        return self._phone

    def get_mail(self) -> str | None:
        return self._mail
