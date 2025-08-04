package com.example.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.example.model.Priority;
import com.example.model.Todo;
import com.example.service.TodoService;

public class TodoConsoleApp {
    private final TodoService todoService;

    public TodoConsoleApp(TodoService todoService) {
        this.todoService = todoService;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                displayMenu();
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    if (!handleMenuChoice(scanner, choice)) {
                        return;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid menu number (1-13)");
                    scanner.nextLine();
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayMenu() {
        System.out.println("\n ===== TODO APP =====");
        System.out.println("Choose a menu: ");
        System.out.println("1) Add Todo");
        System.out.println("2) List Todo");
        System.out.println("3) Update Todo");
        System.out.println("4) Remove Todo");
        System.out.println("5) Toggle Complete");
        System.out.println("6) Pending Todos");
        System.out.println("7) Mark All Todos");
        System.out.println("8) Clear All Todos");
        System.out.println("9) Search Todo");
        System.out.println("10) Statistics");
        System.out.println("11) Set Todo Priority");
        System.out.println("12) Todos By Priority");
        System.out.println("13) Exit");
        System.out.print("Your choice: ");
    }

    private boolean handleMenuChoice(Scanner scanner, int choice) {
        switch (choice) {
            case 1:
                System.out.print("Enter the title: ");
                String title = scanner.nextLine();
                if (title.trim().isEmpty()) {
                    System.out.println("Title cannot be empty");
                    break;
                }
                todoService.save(title);
                System.out.println("Todo added successfully");
                break;
            case 2:
                var todos = todoService.findAll();
                if (todos.isEmpty()) {
                    System.out.println("No todos available");
                    break;
                } else {
                    System.out.println("Total todos: " + todos.size());
                    todos.forEach(System.out::println);
                }
                break;
            case 3:
                Todo updateTodo = getValidTodo(scanner, "update");
                if (updateTodo == null) {
                    break;
                }
                System.out.print("Enter new title: ");
                String newTitle = scanner.nextLine();
                if (newTitle.trim().isEmpty()) {
                    System.out.println("Title cannot be empty");
                    break;
                }
                todoService.update(updateTodo.getId(), newTitle);
                System.out.println("Todo updated successfully");
                break;
            case 4:
                Todo removeTodo = getValidTodo(scanner, "remove");
                if (removeTodo == null) {
                    break;
                }
                if (todoService.remove(removeTodo.getId())) {
                    System.out.println("Todo removed successfully");
                    break;
                }
                System.out.println("Todo did not remove");
                break;

            case 5:
                Todo toggleTodo = getValidTodo(scanner, "toggle complete");
                if (toggleTodo == null) {
                    break;
                }
                Todo updatedTodo = todoService.toggleComplete(toggleTodo.getId());
                if (updatedTodo != null) {
                    System.out
                            .println("Todo status toggled to: " + (updatedTodo.isComplete() ? "Completed" : "Pending"));
                } else {
                    System.out.println("Failed to toggle todo status");
                }
                break;
            case 6:
                List<Todo> pendingTodos = todoService.pendingTodos();
                if (pendingTodos.isEmpty()) {
                    System.out.println("No pending todos! All completed.");
                    break;
                }
                System.out.println("Pending Todos: ");
                pendingTodos.forEach(System.out::println);
                break;
            case 7:
                if (isEmpty()) {
                    System.out.println("No todos to mark as complete");
                    break;
                }
                todoService.markAllCompleted();
                System.out.println("All todos marked as done");
                break;
            case 8:
                if (isEmpty()) {
                    System.out.println("Todo list is already empty");
                    break;
                }
                todoService.clear();
                System.out.println("All todos removed");
                break;
            case 9:
                System.out.print("Enter search keyword: ");
                String searchTodoTitle = scanner.nextLine();

                if (searchTodoTitle.trim().isEmpty()) {
                    System.out.println("Search keyword cannot be empty");
                    break;
                }
                List<Todo> foundTodos = todoService.searchByTitle(searchTodoTitle);
                if (foundTodos.isEmpty()) {
                    System.out.println("No todos found containing: '" + searchTodoTitle + "'");
                    break;
                }
                System.out.println("Found " + foundTodos.size() + " todos");
                foundTodos.forEach(System.out::println);
                break;
            case 10:
                todoService.getStatistics();
                break;
            case 11:
                Todo priorityTodo = getValidTodo(scanner, "set priority");
                if (priorityTodo == null) {
                    break;
                }
                System.out.println("Todo: " + priorityTodo.getTitle());
                System.out.println("Current priority: " + priorityTodo.getPriority());
                System.out.println("Available priorities: LOW, MEDIUM, HIGH");
                System.out.print("Enter priority: ");
                String priorityString = scanner.nextLine().trim().toUpperCase();

                if (!validatePriority(priorityString)) {
                    System.out.println("Invalid Priority '" + priorityString + "'! Please use: LOW, MEDIUM, HIGH");
                    break;
                }

                Priority newPriority = Priority.valueOf(priorityString);
                if (newPriority == priorityTodo.getPriority()) {
                    System.out.println("Priority is already: " + newPriority);
                    break;
                }
                
                todoService.setPriority(priorityTodo.getId(), Priority.valueOf(priorityString));
                System.out.println("Priority updated from " + priorityTodo.getPriority() + " to " + priorityString);
                break;
            case 12:
                System.out.println("Enter the priority: ");
                String priority = scanner.nextLine().toUpperCase();
                if(priority.trim().isEmpty()) {
                    System.out.println("Priority cannot be empty");
                    break;
                }
                List<Todo> listByPriority = todoService.findByPriority(Priority.valueOf(priority));
                if(listByPriority.isEmpty()) {
                    System.out.println("There is no todo with priorty: "+priority);
                    break;
                }

                listByPriority.forEach(todo -> System.out.println(todo.toString()));
                break;
            case 13:
                System.out.println("Todtodo is closing...");
                return false;
            default:
                System.out.println("Invalid choice!");
                break;
        }
        return true;
    }

    private boolean isEmpty() {
        return todoService.isEmpty();
    }

    private boolean isNegativeId(Long id) {
        return id < 0L;
    }

    private boolean validatePriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            return false;
        }
        try {
            Priority.valueOf(priority);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Todo getValidTodo(Scanner scanner, String action) {
        Long id = getValidTodoId(scanner, action);
        if (id == null) {
            return null;
        }
        Optional<Todo> todo = todoService.findById(id);
        if (todo.isEmpty()) {
            System.out.println("Todo not found with ID: " + id);
            return null;
        }

        return todo.get();
    }

    private Long getValidTodoId(Scanner scanner, String action) {
        System.out.print("Enter the ID" + (action != null ? " to " + action : "") + ": ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();
            if (isNegativeId(id)) {
                System.out.println("ID cannot be negative");
                return null;
            }
            return id;

        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number");
            scanner.nextLine();
            return null;
        }
    }


}
