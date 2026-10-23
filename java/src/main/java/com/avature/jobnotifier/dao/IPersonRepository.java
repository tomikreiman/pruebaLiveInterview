package com.avature.jobnotifier.dao;

import com.avature.jobnotifier.domain.Person;

import java.sql.SQLException;
import java.util.List;

public interface IPersonRepository {
    List<Person> getAll() throws SQLException;
}
