package com.example.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.model.Priority;
import com.example.model.Todo;

@Component
public interface TodoService {
    List<Todo> findAll();
    Todo save(String title);
    Optional<Todo> findById(Long id);
    Todo update(Long id, String title);
    boolean remove(Long id);
    Todo toggleComplete(Long id);
    int totalTodo();
    List<Todo> pendingTodos();
    void markAllCompleted();
    void clear();
    boolean isEmpty();
    List<Todo> searchByTitle(String title);
    void getStatistics();
    Todo setPriority(Long id, Priority priority);
    List<Todo> findByPriority(Priority priority);
    Map<Long, Todo> loadTodos();
    void onDataChanged();
}
