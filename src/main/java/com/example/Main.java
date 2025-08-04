package com.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.config.AppConfig;
import com.example.service.TodoService;
import com.example.ui.TodoConsoleApp;

public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TodoService todoService = context.getBean(TodoService.class);
            TodoConsoleApp consoleApp = new TodoConsoleApp(todoService);
            consoleApp.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}