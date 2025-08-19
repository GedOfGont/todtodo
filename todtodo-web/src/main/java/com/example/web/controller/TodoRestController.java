package com.example.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Todo;
import com.example.service.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoRestController {

    private final TodoService todoService;

    public TodoRestController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(value = "/findAll")
    public List<Todo> findAll(){
        return todoService.findAll();
    }

    @GetMapping(value = "/refresh")
    public void refresh() {
        todoService.refresh();
    }

}
