package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.service.TodoService;
import com.example.service.impl.InMemoryTodoServiceImpl;

@Configuration
public class AppConfig {
    
    @Bean
    public TodoService todoService() {
        return new InMemoryTodoServiceImpl();
    }

}
