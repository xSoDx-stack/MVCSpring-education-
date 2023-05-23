package ru.example.mvcspringeducation.dao;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.example.mvcspringeducation.model.Book;
import ru.example.mvcspringeducation.model.Person;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private PersonDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        return jdbcTemplate.query(
                "SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(UUID person_id) {
       return jdbcTemplate.query(
               "SELECT * FROM Person WHERE person_id=?",new BeanPropertyRowMapper<>(Person.class), person_id)
               .stream().findAny().orElse(null);
    }
    public List<Book> getOwnedBooks(UUID person_id){
        return jdbcTemplate.query(
                "SELECT * FROM book WHERE person_id=?",
                new BeanPropertyRowMapper<>(Book.class), person_id);
    }

    public Optional<Person> show(String ful_name){
        return jdbcTemplate.query(
                "SELECT * FROM Person WHERE full_name=?", new BeanPropertyRowMapper<>(Person.class), ful_name)
                .stream().findAny();
    }

     public void save(Person person){
        jdbcTemplate.update("INSERT INTO person(full_name, age) Values(?,?)",
                person.getFullName(),person.getAge());
     }

     public void update(UUID person_id, Person updatePerson){
        jdbcTemplate.update("UPDATE person SET full_name=?,age=? WHERE person_id=?",
                updatePerson.getFullName(),updatePerson.getAge(), person_id);
     }

     public void delete(UUID person_id){
        jdbcTemplate.update("DELETE FROM person WHERE person_id=?", person_id);
     }
     ////////////////////////////////////////////////////////////////////
    //////////////////Наполнение таблицы тестовыми данными//////
    ////////////////////////////////////////////////////////////////////

    public void testBatchUpdate(int size){
        List<Person> people = createRandomPeople(size);
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO person(full_name, age) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, people.get(i).getFullName());
                ps.setInt(2, people.get(i).getAge());
            }
            @Override
            public int getBatchSize() {
                return people.size();
            }
        });
    }

    private List<Person> createRandomPeople(int size) {
        List<Person> people = new ArrayList<>();
        Faker faker = new Faker(new Locale("ru"));
        for(int i = 0; i < size; i++){
            people.add(new Person(
                    faker.name().fullName(),
                    faker.number().numberBetween(1900,2020))
            );
        }
        return people;
    }
}
