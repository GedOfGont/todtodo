package com.example.service.impl;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.model.Todo;

@Component
@Profile(value = "memory")
public class InMemoryTodoServiceImpl extends AbstractTodoService {

    @Override
    public void onDataChanged() {
         System.out.println("No saved file");
    }

    @Override
    public Map<Long, Todo> loadTodos() {
        return null;
    }
}
