package ru.example.mvcspringeducation.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Person {
    private UUID id = UUID.randomUUID();
    @NotEmpty(message = "Поле имени не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть длинее 2х символов")
    private String name;
    @NotEmpty(message = "Поле фамилия не должно быть пустым")
    @Size(min = 2, max = 30, message = "Фамилия должна быть длинее 2х символов")
    private String surname;
    @NotEmpty(message = "Поле почты не должно быть пустым")
    @Email(message = "Неправильный формат почты")
    private String email;

    public Person(){
    }

    public Person(String name, String surname, String email){
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getSurname(){
        return surname;
    }
    public void setSurname(String surname){
        this.surname = surname;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
}
