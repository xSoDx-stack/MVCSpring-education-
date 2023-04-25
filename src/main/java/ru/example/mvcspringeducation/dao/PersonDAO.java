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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


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

     public void save(Person person){
        jdbcTemplate.update("INSERT INTO person(name, surname, age, email) Values(?,?,?,?)",
                person.getName(),person.getSurname(),person.getAge(), person.getEmail());
     }

     public void update(UUID id, Person updatePerson){
        jdbcTemplate.update("UPDATE person SET name=?, surname=?, age=?, email=? WHERE id=?",
                updatePerson.getName(),updatePerson.getSurname(),updatePerson.getAge(), updatePerson.getEmail(),id);
     }

     public void delete(UUID id){
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
     }
     ////////////////////////////////////////////////////////////////////
    //////////////////тестируем производительность пакетной вставки//////
    ////////////////////////////////////////////////////////////////////

    public void testMultipleUpdate(){
        List<Person> people = create1000People();
        long before = System.currentTimeMillis();
        for(Person person : people){
            jdbcTemplate.update("INSERT INTO person(name, surname, age, email) Values(?,?,?,?)",
                    person.getName(),person.getSurname(), person.getAge(), person.getEmail());
        }
        long after = System.currentTimeMillis();

        System.out.println("Time: " + ((after - before)/60));
    }

    public void testBatchUpdate(){
        List<Person> people = create1000People();
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO person(name, surname, age, email) VALUES (?,?,?,?)",
                new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, people.get(i).getName());
                ps.setString(2, people.get(i).getSurname());
                ps.setInt(3, people.get(i).getAge());
                ps.setString(4, people.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });
        long after = System.currentTimeMillis();
        System.out.println("Time: " + ((after - before)/60));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();
        Faker faker = new Faker();
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"),new RandomService());

        for(int i = 0; i < 1000; i++){
            people.add(new Person(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.number().numberBetween(12,90),
                    fakeValuesService.bothify("????##@gmail.com")));
        }
        return people;
    }


}
