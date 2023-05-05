package ru.example.mvcspringeducation.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.example.mvcspringeducation.dao.PersonDAO;
import ru.example.mvcspringeducation.model.Person;

@Component
public class PersonValidation implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidation(PersonDAO personDAO){
        this.personDAO = personDAO;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(personDAO.show(person.getEmail()).isPresent()){
            errors.rejectValue("email","","Эта почта уже используется");
        }
    }
}
