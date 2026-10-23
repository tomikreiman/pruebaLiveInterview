package com.avature.jobnotifier.dao;

import com.avature.jobnotifier.domain.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository implements IPersonRepository {
    private final Connection connection;

    public PersonRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Person> getAll() throws SQLException {
        String sql = "SELECT id, name, personal_tag, mail, phone FROM people";
        List<Person> people = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rows = stmt.executeQuery(sql)) {
            while (rows.next()) {
                people.add(new Person(
                        rows.getString("name"),
                        rows.getString("personal_tag"),
                        rows.getString("mail"),
                        rows.getString("phone")
                ));
            }
        }

        return people;
    }
}
