package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.service.TodoService;
import com.example.service.impl.FileBasedTodoServiceImpl;
import com.example.service.impl.InMemoryTodoServiceImpl;

@Configuration
public class AppConfig {
    
    @Bean
    @Profile(value = {"file", "default"})
    public TodoService fileBasedTodoService() {
        return new FileBasedTodoServiceImpl();
    }

    @Bean
    @Profile(value = "memory")
    public TodoService inMemoryTodoService() {
        return new InMemoryTodoServiceImpl();
    }

}
