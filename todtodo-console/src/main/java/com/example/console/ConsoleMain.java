package com.example.console;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.config.AppConfig;
import com.example.console.ui.TodoConsoleApp;
import com.example.service.TodoService;

public class ConsoleMain {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("file");
            context.register(AppConfig.class);
            context.refresh();

            TodoService service = context.getBean(TodoService.class);
            TodoConsoleApp app = new TodoConsoleApp(service, context.getEnvironment());
            app.start();
        } catch (Exception e) {
            System.err.println("Console application failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}