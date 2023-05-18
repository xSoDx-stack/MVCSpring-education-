package ru.example.mvcspringeducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.example.mvcspringeducation.dao.BookDAO;
import ru.example.mvcspringeducation.model.Book;

import java.util.UUID;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("book", bookDAO.index());
        return "book/index";
    }

    @GetMapping({"/{id}"})
    public String show(@PathVariable("id") UUID book_id, Model model){
        model.addAttribute("book", bookDAO.show(book_id));
        if(bookDAO.personBook(bookDAO.show(book_id).getPerson_id()) != null){
            model.addAttribute("person", bookDAO.personBook(bookDAO.show(book_id).getPerson_id()));
        }
        System.out.println(bookDAO.personBook(bookDAO.show(book_id).getPerson_id()));
        return "book/show";
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

