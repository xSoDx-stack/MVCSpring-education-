package ru.example.mvcspringeducation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/hello-world")
    public String sayHello(){
        System.out.println("Я работаю");
        return "hello-world";
    }
}
