package ru.example.mvcspringeducation.dao;

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
        jdbcTemplate.update("INSERT INTO person Values(?,?,?,?)",
                person.getId(), person.getName(),person.getSurname(), person.getEmail());
     }

     public void update(UUID id, Person updatePerson){
        jdbcTemplate.update("UPDATE person SET name=?, surname=?, email=? WHERE id=?",
                updatePerson.getName(),updatePerson.getSurname(), updatePerson.getEmail(),id);
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
            jdbcTemplate.update("INSERT INTO person Values(?,?,?,?)",
                    person.getId(), person.getName(),person.getSurname(), person.getEmail());
        }
        long after = System.currentTimeMillis();

        System.out.println("Time: " + ((after - before)/60));
    }

    public void testBatchUpdate(){
        List<Person> people = create1000People();
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO person VALUES (?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, people.get(i).getId());
                ps.setString(2, people.get(i).getName());
                ps.setString(3, people.get(i).getSurname());
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
        for(int i = 0; i < 1000; i++){
            people.add(new Person("Name" + i, "Surname" + i,"name" + i + "@mail.ru"));
        }
        return people;
    }


}
