package ru.example.mvcspringeducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.example.mvcspringeducation.dao.BookDAO;
import ru.example.mvcspringeducation.dao.PersonDAO;
import ru.example.mvcspringeducation.model.Book;
import ru.example.mvcspringeducation.model.Person;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("book", bookDAO.index());
        return "book/index";
    }

    @GetMapping({"/{id}"})
    public String show(@PathVariable("id") UUID book_id, Model model,
                       @ModelAttribute("person") Person person){
        model.addAttribute("book", bookDAO.show(book_id));

        Optional<Person> bookOwner = bookDAO.getOwnerBook(book_id);
        if(bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", personDAO.index());
        return "book/show";
        }

    @PatchMapping("/{id}/appoint")
    public String makeBook(@ModelAttribute("person") Person person,
                           @PathVariable("id") UUID book_id){
        bookDAO.appointOwnerBook(book_id, person.getPerson_id());
        return "redirect:/book/{id}";
    }
    @DeleteMapping("/{id}/delete")
    public String deletePersonBook(@PathVariable("id") UUID book_id){
        bookDAO.deleteOwnerBook(book_id);
        return "redirect:/book/{id}";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "book/new";
        }
        bookDAO.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") UUID book_id){
        model.addAttribute("book", bookDAO.show(book_id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") UUID book_id) {
            if (bindingResult.hasErrors()) {
                return "book/edit";
            }
        bookDAO.update(book_id, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") UUID book_id){
        bookDAO.delete(book_id);
        return "redirect:/book";
    }




}

