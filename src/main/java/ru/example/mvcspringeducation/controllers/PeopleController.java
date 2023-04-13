package ru.example.mvcspringeducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.example.mvcspringeducation.dao.PersonDAO;
import ru.example.mvcspringeducation.model.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO){
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        System.out.println("Use controller index");
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        System.out.println("Use controller show");
        model.addAttribute("person",personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        System.out.println("Use controller new");
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") Person person){
        System.out.println("Use controller create");
        personDAO.save(person);
        return "redirect:/people";
    }
}
