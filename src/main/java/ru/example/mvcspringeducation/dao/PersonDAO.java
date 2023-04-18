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
        Statement statement = connection.createStatement();
        String SQL = "SELECT * FROM Person";
        ResultSet resultSet = statement.executeQuery(SQL);

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


    public Person show(UUID id){
//        return people.stream().filter(p -> p.getId().equals(id)).findAny().orElse(null);
        return null;
    }
     public void save(Person person) throws SQLException {
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement =
                connection.prepareStatement("INSERT INTO Person VALUES(uuid_generate_v4(),?,?,?)");
        preparedStatement.setString(1, person.getName());
        preparedStatement.setString(2, person.getSurname());
        preparedStatement.setString(3, person.getEmail());

        preparedStatement.executeUpdate();
     }

     public void update(UUID id, Person updatePerson){
//        Person personToBeUpdated = show(id);
//        personToBeUpdated.setName(updatePerson.getName());
//        personToBeUpdated.setSurname(updatePerson.getSurname());
//        personToBeUpdated.setEmail(updatePerson.getEmail());
     }

     public void delete(UUID id){
//        people.removeIf(p -> p.getId().equals(id));
     }
}
