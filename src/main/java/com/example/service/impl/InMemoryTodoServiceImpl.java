package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.model.Priority;
import com.example.model.Todo;
import com.example.service.TodoService;

public class InMemoryTodoServiceImpl implements TodoService {

    private Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Todo save(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title can not be empty");
        }
        Long newTodoId = idCounter.getAndIncrement();
        Todo newTodo = new Todo(newTodoId, title, false);
        todos.put(newTodoId, newTodo);
        return newTodo;
    }

    @Override
    public Todo update(Long id, String title) {
        if (id == null) {
            throw new IllegalArgumentException("ID is not valid");
        }

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title can not be empty");
        }

        Todo updateTodo = findById(id).orElseThrow(() -> new IllegalArgumentException("Todo not found"));
        updateTodo.setTitle(title);
        return updateTodo;
    }

    @Override
    public Optional<Todo> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is not valid");
        }
        return Optional.ofNullable(todos.get(id));
    }

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    @Override
    public boolean remove(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is not valid");
        }
        return todos.remove(id) != null;
    }

    @Override
    public int totalTodo() {
        return todos.size();
    }

    @Override
    public Todo toggleComplete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is not valid");
        }
        Todo toggleTodo = findById(id).orElse(null);
        if (toggleTodo == null) {
            return null;
        }
        toggleTodo.setComplete(!toggleTodo.isComplete());
        return toggleTodo;
    }

    @Override
    public List<Todo> pendingTodos() {
        List<Todo> pendindTodos = findAll().stream().filter(t -> t.isComplete() == false).collect(Collectors.toList());
        return pendindTodos;
    }

    @Override
    public List<Todo> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title can not be empty");
        }

        List<Todo> foundTodos = findAll().stream().filter(t -> t.getTitle().contains(title))
                .collect(Collectors.toList());
        return foundTodos;
    }

    @Override
    public void markAllCompleted() {
        if (isEmpty()) {
            throw new NoSuchElementException("Todo list is empty");
        }
        findAll().stream().forEach(t -> t.setComplete(true));
    }

    @Override
    public void clear() {
        if (isEmpty()) {
            throw new NoSuchElementException("Todo list is empty");
        }
        todos.clear();
    }

    @Override
    public boolean isEmpty() {
        return todos.isEmpty();
    }

    @Override
    public void getStatistics() {
        int totalTodo = todos.size();
        System.out.println(" ===== TODO STATISTICS =====");

        if (totalTodo == 0) {
            System.out.println("No todos available - Add some todos first!");
            return;
        }

        int pendingTodo = (int) todos.values().stream().filter(t -> !t.isComplete()).count();
        int completedTodo = (int) todos.values().stream().filter(t -> t.isComplete()).count();

        System.out.println("Total todos : " + totalTodo);
        System.out.println("Pending todos : " + pendingTodo);
        System.out.println("Completed todos : " + completedTodo);
        System.out.println("Completion rate : " + Math.round((completedTodo / (double) totalTodo) * 100) + "%");
        System.out.println("High priority : "+findByPriority(Priority.valueOf("HIGH")).size());
        System.out.println("Medium priority : "+findByPriority(Priority.valueOf("MEDIUM")).size());
        System.out.println("Low priority : "+findByPriority(Priority.valueOf("LOW")).size());
    }

    @Override
    public Todo setPriority(Long id, Priority priority) {
        if (id == null) {
            throw new IllegalArgumentException("ID is not valid");
        }

        Todo priorityTodo = findById(id).orElse(null);
        if (priorityTodo == null) {
            return null;
        }
        priorityTodo.setPriority(priority);
        return priorityTodo;
    }

    @Override
    public List<Todo> findByPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority is not valid");
        }
        return todos.values().stream().filter(todo -> todo.getPriority().equals(priority)).collect(Collectors.toList());
    }
}
