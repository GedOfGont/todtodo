package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.model.Priority;
import com.example.model.Todo;
import com.example.model.TodoStatistics;
import com.example.service.TodoService;

public abstract class AbstractTodoService implements TodoService {

    protected Map<Long, Todo> todos = new ConcurrentHashMap<>();
    protected final AtomicLong idCounter = new AtomicLong(1L);

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    @Override
    public Optional<Todo> save(String title) {
        validateTitle(title);
        Long newTodoId = idCounter.getAndIncrement();
        Todo newTodo = new Todo(newTodoId, title, false);
        todos.put(newTodoId, newTodo);
        onDataChanged();
        return Optional.of(newTodo);

    }

    @Override
    public Optional<Todo> findById(Long id) {
        validateId(id);
        return Optional.ofNullable(todos.get(id));
    }

    @Override
    public Optional<Todo> update(Long id, String title) {
        validateId(id);
        validateTitle(title);
        Optional<Todo> updateTodo = findById(id);
        if (updateTodo.isPresent()) {
            updateTodo.get().setTitle(title);
            onDataChanged();
            return updateTodo;
        }
        return Optional.empty();
    }

    @Override
    public boolean remove(Long id) {
        validateId(id);
        Todo removeTodo = todos.remove(id);
        if (removeTodo != null) {
            onDataChanged();
        }
        return removeTodo != null;

    }

    @Override
    public Optional<Todo> toggleComplete(Long id) {
        validateId(id);
        Optional<Todo> toggleTodo = findById(id);
        if (toggleTodo.isPresent()) {
            toggleTodo.get().setComplete(!toggleTodo.get().isComplete());
            onDataChanged();
            return toggleTodo;
        }
        return Optional.empty();
    }

    @Override
    public int getTotalCount() {
        return todos.size();
    }

    @Override
    public List<Todo> pendingTodos() {
        return todos.values().stream().filter(t -> !t.isComplete()).toList();
    }

    @Override
    public void markAllCompleted() {
        todos.values().forEach(t -> t.setComplete(true));
        onDataChanged();
    }

    @Override
    public void clear() {
        if (todos.isEmpty()) {
            return;
        }
        todos.clear();
        idCounter.set(1L);
        onDataChanged();
    }

    @Override
    public boolean isEmpty() {
        return todos.isEmpty();
    }

    @Override
    public List<Todo> searchByTitle(String title) {
        validateTitle(title);
        return todos.values().stream().filter(t -> t.getTitle().toLowerCase().contains(title.toLowerCase())).toList();
    }

    @Override
    public TodoStatistics getStatistics() {
        if (todos.isEmpty()) {
            return new TodoStatistics(0, 0, 0, 0, 0, 0);
        }
        var completionStats = todos.values().stream().collect(
                (Collectors.groupingBy(todo -> todo.isComplete() ? "completed" : "pending", Collectors.counting())));

        var priorityStats = todos.values().stream().collect(Collectors.groupingBy(Todo::getPriority, Collectors.counting()));

        TodoStatistics todoStatistics = new TodoStatistics(todos.size(),
                completionStats.getOrDefault("completed", 0L).intValue(),
                completionStats.getOrDefault("pending", 0L).intValue(),
                priorityStats.getOrDefault(Priority.LOW, 0L).intValue(),
                priorityStats.getOrDefault(Priority.MEDIUM, 0L).intValue(),
                priorityStats.getOrDefault(Priority.HIGH, 0L).intValue());

        return todoStatistics;
    }

    @Override
    public Optional<Todo> setPriority(Long id, Priority priority) {
        validateId(id);
        validatePriority(priority);
        Optional<Todo> priorityTodo = findById(id);
        if (priorityTodo.isPresent()) {
            priorityTodo.get().setPriority(priority);
            onDataChanged();
            return priorityTodo;
        }
        return Optional.empty();
    }

    @Override
    public List<Todo> findByPriority(Priority priority) {
        validatePriority(priority);
        return todos.values().stream().filter(t -> t.getPriority().equals(priority)).toList();
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        if(id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
    }

    private void validatePriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        } 
    }

    protected void setInitialData(Map<Long, Todo> initialTodos) {
        this.todos.clear();
        this.todos.putAll(initialTodos);
    }

    @Override
    public void refresh() {
        loadTodos();
    }

    public abstract void onDataChanged();

    public abstract Map<Long, Todo> loadTodos();
}
