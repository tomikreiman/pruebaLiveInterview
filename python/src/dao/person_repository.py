import sqlite3

from src.domain.person import Person
from src.dao.i_person_repository import IPersonRepository


class PersonRepository(IPersonRepository):
    def __init__(self, db: sqlite3.Connection):
        self._db = db

    def get_all(self) -> list[Person]:
        cursor = self._db.execute(
            "SELECT id, name, personal_tag, mail, phone FROM people"
        )
        rows = cursor.fetchall()

        people = []
        for row in rows:
            people.append(
                Person(
                    row["name"],
                    row["personal_tag"],
                    row["mail"],
                    row["phone"],
                )
            )

        return people
