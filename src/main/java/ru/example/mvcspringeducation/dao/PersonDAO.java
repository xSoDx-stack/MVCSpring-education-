package ru.example.mvcspringeducation.dao;

import org.springframework.stereotype.Component;
import ru.example.mvcspringeducation.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class PersonDAO {
    private final List<Person> people = new ArrayList<>();

    {
        people.add(new Person("Tom", "Goward", "tom@mail.ru"));
        people.add(new Person("Gary", "Fingers", "gary@mail.ru"));
        people.add(new Person("Bob","Hilton", "bob@mail.ru"));
    }
    public List<Person> index(){
        return people;
    }
    public Person show(UUID id){
        return people.stream().filter(p -> p.getId().equals(id)).findAny().orElse(null);
    }

     public void save(Person person){
        people.add(person);
     }

     public void update(UUID id, Person updatePerson){
        Person personToBeUpdated = show(id);
        personToBeUpdated.setName(updatePerson.getName());
        personToBeUpdated.setSurname(updatePerson.getSurname());
        personToBeUpdated.setEmail(updatePerson.getEmail());
     }

     public void delete(UUID id){
        people.removeIf(p -> p.getId().equals(id));
     }
}
