package ru.example.mvcspringeducation.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.example.mvcspringeducation.dao.PersonDAO;
import ru.example.mvcspringeducation.model.Person;
import ru.example.mvcspringeducation.util.PersonValidation;

import java.util.UUID;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidation personValidation;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidation personValidation){
        this.personDAO = personDAO;
        this.personValidation = personValidation;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") UUID person_id, Model model){
        model.addAttribute("person", personDAO.show(person_id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        personValidation.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") UUID person_id){
        model.addAttribute("person", personDAO.show(person_id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") UUID person_id){
         if(!personDAO.show(person_id).getFullName().equals(person.getFullName())) {
             personValidation.validate(person, bindingResult);
             if (bindingResult.hasErrors()) {
                 return "people/edit";
             }
         }
        personDAO.update(person_id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{person_id}")
    public String delete(@PathVariable("person_id") UUID person_id){
        personDAO.delete(person_id);
        return "redirect:/people";
    }
}
