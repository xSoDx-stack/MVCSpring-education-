package ru.example.mvcspringeducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.mvcspringeducation.dao.PersonDAO;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController{
    private final PersonDAO personDAO;

    @Autowired
    public BatchController(PersonDAO personDAO){
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(){

        return "batch/index";
    }

    @RequestMapping()
    public String withoutBatchUpdate(
            @RequestParam(value = "size") int size){
        if (size < 0){
            return "batch/index";
        }
        personDAO.testBatchUpdate(size);
        return "redirect:/people";
    }
}
