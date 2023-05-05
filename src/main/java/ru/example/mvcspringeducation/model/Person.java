package ru.example.mvcspringeducation.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Person {
    private UUID id;
    @NotEmpty(message = "Поле имени не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть длинее 2х символов")
    private String name;
    @NotEmpty(message = "Поле фамилия не должно быть пустым")
    @Size(min = 2, max = 30, message = "Фамилия должна быть длинее 2х символов")
    private String surname;
    private int age;

    @NotEmpty(message = "Поле почты не должно быть пустым")
    @Email(message = "Неправильный формат почты")
    private String email;

    @NotEmpty(message = "Поле адресса не должно быть пустым")
    //Страна, Город, Индекс(6 цифр)
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message =
            "Ваш адрес не соответсвует формату: Страна, Город, почтовый индекс 6 циффр")
    private String address;


    public Person(){
    }

    public Person(String name, String surname, int age, String email, String address){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.address = address;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
