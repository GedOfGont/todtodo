package com.example.model;

import java.util.Objects;

public class Todo {

    private Long id;
    private String title;
    private boolean complete;
    private Priority priority;

    public Todo() {
        this.priority = Priority.LOW;
    }

    public Todo(Long id, String title, boolean complete) {
        this.id = id;
        this.title = title;
        this.complete = complete;
        this.priority = Priority.LOW;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isComplete() {
        return complete;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        stringBuilder.append("id=").append(id).append(", title=").append(title).append(", complete=").append(complete)
                .append(", priority=").append(priority).append("]");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Todo todo = (Todo) obj;
        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
