package ru.example.mvcspringeducation.dao;

import org.springframework.stereotype.Component;
import ru.example.mvcspringeducation.model.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private final List<Person> people = new ArrayList<>();

    {
        people.add(new Person(++PEOPLE_COUNT, "Tom", "Goward", "tom@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Gary", "Fingers", "gary@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Bob","Hilton", "bob@mail.ru"));
    }
    public List<Person> index(){
        return people;
    }
    public Person show(int id){
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }
     public void save(Person person){
        person.setId(++PEOPLE_COUNT);
        people.add(person);
     }
}
