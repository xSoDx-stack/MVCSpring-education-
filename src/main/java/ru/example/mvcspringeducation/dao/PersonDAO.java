package ru.example.mvcspringeducation.dao;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
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
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(UUID id) {
       return jdbcTemplate.query(
               "SELECT * FROM Person WHERE id=?",new BeanPropertyRowMapper<>(Person.class),id)
               .stream().findAny().orElse(null);
    }
    public Optional<Person> show(String email){
        return jdbcTemplate.query(
                "SELECT * FROM Person WHERE email=?", new BeanPropertyRowMapper<>(Person.class),email)
                .stream().findAny();
    }

     public void save(Person person){
        jdbcTemplate.update("INSERT INTO person(name, surname, age, email, address) Values(?,?,?,?,?)",
                person.getName(),person.getSurname(),person.getAge(), person.getEmail(), person.getAddress());
     }

     public void update(UUID id, Person updatePerson){
        jdbcTemplate.update("UPDATE person SET name=?, surname=?, age=?, email=?, address=? WHERE id=?",
                updatePerson.getName(),updatePerson.getSurname(),updatePerson.getAge(), updatePerson.getEmail(),
                updatePerson.getAddress(), id);
     }

     public void delete(UUID id){
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
     }
     ////////////////////////////////////////////////////////////////////
    //////////////////Наполнение таблицы тестовыми данными//////
    ////////////////////////////////////////////////////////////////////

    public void testBatchUpdate(int size){
        List<Person> people = create1000People(size);
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO person(name, surname, age, email, address) VALUES (?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, people.get(i).getName());
                ps.setString(2, people.get(i).getSurname());
                ps.setInt(3, people.get(i).getAge());
                ps.setString(4, people.get(i).getEmail());
                ps.setString(5,people.get(i).getAddress());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });
        long after = System.currentTimeMillis();
        System.out.println("Time: " + ((after - before)/60));
    }

    private List<Person> create1000People(int size) {
        List<Person> people = new ArrayList<>();
        Faker faker = new Faker();
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"),
                new RandomService());

        for(int i = 0; i < size; i++){
            people.add(new Person(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.number().numberBetween(12,90),
                    fakeValuesService.bothify("????##@gmail.com"),
                    faker.address().fullAddress())
                    );

        }
        return people;
    }


}
