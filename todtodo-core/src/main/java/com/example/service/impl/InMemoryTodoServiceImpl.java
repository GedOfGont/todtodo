package com.example.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.model.Todo;

@Component
@Profile(value = "memory")
public class InMemoryTodoServiceImpl extends AbstractTodoService {

    @Override
    public void onDataChanged() {
    }

    @Override
    public Map<Long, Todo> loadTodos() {
        return new ConcurrentHashMap<>();
    }
}
