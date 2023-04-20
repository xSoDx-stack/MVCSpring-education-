package ru.example.mvcspringeducation.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.example.mvcspringeducation.model.Person;

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
}
