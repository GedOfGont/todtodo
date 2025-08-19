package com.example.model;

public class TodoStatistics {
    private final int totalTodos;
    private final int completedTodos;
    private final int pendingTodos;
    private final int lowPriorityTodos;
    private final int mediumPriorityTodos;
    private final int highPriorityTodos;

    public TodoStatistics(int totalTodos, int completedTodos, int pendingTodos, int lowPriorityTodos,
            int mediumPriorityTodos, int highPriorityTodos) {
        this.totalTodos = totalTodos;
        this.completedTodos = completedTodos;
        this.pendingTodos = pendingTodos;
        this.lowPriorityTodos = lowPriorityTodos;
        this.mediumPriorityTodos = mediumPriorityTodos;
        this.highPriorityTodos = highPriorityTodos;
    }

    public int getTotalTodos() {
        return totalTodos;
    }

    public int getCompletedTodos() {
        return completedTodos;
    }

    public int getPendingTodos() {
        return pendingTodos;
    }

    public int getLowPriorityTodos() {
        return lowPriorityTodos;
    }

    public int getMediumPriorityTodos() {
        return mediumPriorityTodos;
    }

    public int getHighPriorityTodos() {
        return highPriorityTodos;
    }

    @Override
    public String toString() {
        return "TodoStatistics{" +
                "totalTodos=" + totalTodos +
                ", completedTodos=" + completedTodos +
                ", pendingTodos=" + pendingTodos +
                ", lowPriorityTodos=" + lowPriorityTodos +
                ", mediumPriorityTodos=" + mediumPriorityTodos +
                ", highPriorityTodos=" + highPriorityTodos +
                '}';
    }
}
