package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.model.Priority;
import com.example.model.Todo;
import com.example.model.TodoStatistics;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public interface TodoService {
    List<Todo> findAll();
    List<Todo> findByPriority(@NotNull Priority priority);
    List<Todo> searchByTitle(@NotNull @NotEmpty String title);
    List<Todo> pendingTodos();
    Optional<Todo> save(@NotNull @NotEmpty String title);
    Optional<Todo> findById(@NotNull @Positive Long id);
    Optional<Todo> update(@NotNull @Positive Long id, @NotNull @NotEmpty String title);
    Optional<Todo> setPriority(@NotNull @Positive Long id, @NotNull Priority priority);
    Optional<Todo> toggleComplete(@NotNull @Positive Long id);
    TodoStatistics getStatistics();
    boolean remove(@NotNull @Positive Long id);
    boolean isEmpty();
    int getTotalCount();
    void markAllCompleted();
    void clear();
    void refresh();
}
