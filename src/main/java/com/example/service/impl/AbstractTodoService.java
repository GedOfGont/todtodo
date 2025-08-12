package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.example.model.Priority;
import com.example.model.Todo;
import com.example.service.TodoService;

public abstract class AbstractTodoService implements TodoService {

    protected Map<Long, Todo> todos = new ConcurrentHashMap<>();
    protected final AtomicLong idCounter = new AtomicLong(1L);

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    @Override
    public Todo save(String title) {
        validateTitle(title);
        Long newTodoId = idCounter.getAndIncrement();
        Todo newTodo = new Todo(newTodoId, title, false);
        todos.put(newTodoId, newTodo);
        onDataChanged();
        return newTodo;

    }

    @Override
    public Optional<Todo> findById(Long id) {
        validateId(id);
        return Optional.ofNullable(todos.get(id));
    }

    @Override
    public Todo update(Long id, String title) {
        validateId(id);
        validateTitle(title);
        Optional<Todo> updateTodo = findById(id);
        if (updateTodo.isPresent()) {
            updateTodo.get().setTitle(title);
            onDataChanged();
            return updateTodo.get();
        }
        return null;
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
    public Todo toggleComplete(Long id) {
        validateId(id);
        Optional<Todo> toggleTodoOptional = findById(id);
        if (toggleTodoOptional.isPresent()) {
            Todo toggleTodo = toggleTodoOptional.get();
            toggleTodo.setComplete(!toggleTodo.isComplete());
            onDataChanged();
            return toggleTodo;
        }
        return null;
    }

    @Override
    public int totalTodo() {
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
            System.out.println("Todo list is already empty");
            return;
        }
        todos.clear();
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
    public void getStatistics() {
        if (todos.isEmpty()) {
            System.out.println("No todos available - Add some todos first");
            return;
        }

        int totalTodo = todos.size();
        int pendingTodo = (int) todos.values().stream().filter(t -> !t.isComplete()).count();
        int completedTodo = totalTodo - pendingTodo;

        System.out.println(" ===== TODO STATISTICS =====");
        System.out.println("Total todos : " + totalTodo);
        System.out.println("Pending todos : " + pendingTodo);
        System.out.println("Completed todos : " + completedTodo);
        System.out.println("Completion rate : " + Math.round((completedTodo / (double) totalTodo) * 100) + "%");
        System.out.println("High priority : " + findByPriority(Priority.HIGH).size());
        System.out.println("Medium priority : " + findByPriority(Priority.MEDIUM).size());
        System.out.println("Low priority : " + findByPriority(Priority.LOW).size());
    }

    @Override
    public Todo setPriority(Long id, Priority priority) {
        validateId(id);
        validatePriority(priority);
        Optional<Todo> priorityTodo = findById(id);
        if (priorityTodo.isPresent()) {
            priorityTodo.get().setPriority(priority);
            onDataChanged();
            return priorityTodo.get();
        }
        return null;
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
    }

    private void validatePriority(Priority priority) {
        if(priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        } else if(priority != Priority.HIGH && priority != Priority.MEDIUM && priority != Priority.LOW) {
            throw new IllegalArgumentException("Priority must be (HIGH/MEDIUM/LOW)");
        }
    }

    protected void setInitialData(Map<Long, Todo> initialTodos, long startingId) {
        this.todos.clear();
        this.todos.putAll(initialTodos);
        this.idCounter.set(startingId);
    }
}
