package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import com.example.config.AppConfig;
import com.example.service.TodoService;
import com.example.ui.TodoConsoleApp;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        try {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
                TodoService todoService = context.getBean(TodoService.class);
                Environment todoEnvironment = context.getEnvironment();
                TodoConsoleApp consoleApp = new TodoConsoleApp(todoService, todoEnvironment);
                consoleApp.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("Fatal error", e);
        }
    }
}