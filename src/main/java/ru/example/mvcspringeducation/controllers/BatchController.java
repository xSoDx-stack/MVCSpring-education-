package ru.example.mvcspringeducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.mvcspringeducation.dao.BookDAO;
import ru.example.mvcspringeducation.dao.PersonDAO;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController{
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    @Autowired
    public BatchController(PersonDAO personDAO, BookDAO bookDAO){
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String people(){

        return "batch/people";
    }

    @RequestMapping()
    public String withoutBatchUpdate(
            @RequestParam(value = "size") int size){
        if (size < 0){
            return "batch/people";
        }
        personDAO.testBatchUpdate(size);
        return "redirect:/people";
    }
    @GetMapping("/book")
    public String book(){

        return "batch/book";
    }

    @RequestMapping("/book")
    public String withoutBatchUpdateBook(
            @RequestParam(value = "size") int size){
        if (size < 0){
            return "batch/book";
        }
        bookDAO.BatchUpdate(size);
        return "redirect:/book";
    }

}
