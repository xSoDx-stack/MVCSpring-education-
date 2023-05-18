package ru.example.mvcspringeducation.model;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Person {
    private UUID person_id;
    @NotEmpty(message = "Поле имени не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть длинее 2х символов")
    private String fullName;

    private int age;


    public Person(){
    }

    public Person(String fullName, int age){
        this.fullName = fullName;
        this.age = age;
    }

    public UUID getPerson_id() {
        return person_id;
    }

    public void setPerson_id(UUID person_id) {
        this.person_id = person_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
