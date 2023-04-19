package ru.example.mvcspringeducation.dao;

import org.springframework.stereotype.Component;
import ru.example.mvcspringeducation.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class PersonDAO {
    private static final String URL = System.getenv("DATASOURCE_URL"); //jdbc:postgresql://localhost:5432/postgres
    private static final String USERNAME = System.getenv("DATASOURCE_USERNAME");
    private static final String PASSWORD = System.getenv("DATASOURCE_PASSWORD");

    private static final Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Person> index() throws SQLException {
        List<Person> people = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Person");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Person person = new Person();
            person.setId(UUID.fromString(resultSet.getString("id")));
            person.setName(resultSet.getString("name"));
            person.setSurname(resultSet.getString("surname"));
            person.setEmail(resultSet.getString("email"));
            people.add(person);
        }
        return people;
    }

    public Person show(UUID id) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM person WHERE id=?");
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Person person = new Person();
        person.setId(id);
        person.setName(resultSet.getString("name"));
        person.setSurname(resultSet.getString("surname"));
        person.setEmail(resultSet.getString("email"));
        return person;
    }

     public void save(Person person) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("INSERT INTO Person VALUES(?,?,?,?)");
        preparedStatement.setObject(1, person.getId());
        preparedStatement.setString(2, person.getName());
        preparedStatement.setString(3, person.getSurname());
        preparedStatement.setString(4, person.getEmail());

        preparedStatement.executeUpdate();
     }

     public void update(UUID id, Person updatePerson) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("UPDATE person SET name=?, surname=?, email=? WHERE id=?");
         preparedStatement.setString(1, updatePerson.getName());
         preparedStatement.setString(2, updatePerson.getSurname());
         preparedStatement.setString(3, updatePerson.getEmail());
         preparedStatement.setObject(4, id);
         preparedStatement.executeUpdate();

     }

     public void delete(UUID id) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("DELETE from person WHERE id=?");
        preparedStatement.setObject(1,id);
        preparedStatement.executeUpdate();
     }
}
