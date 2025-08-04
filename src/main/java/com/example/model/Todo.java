package com.example.model;

public class Todo {

    private Long id;
    private String title;
    private boolean complete;
    private Priority priority;

    public Todo(Long id, String title, boolean complete) {
        this.id = id;
        this.title = title;
        this.complete = complete;
        this.priority = Priority.LOW;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + (complete ? 1231 : 1237);
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Todo other = (Todo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (complete != other.complete)
            return false;
        if (priority != other.priority)
            return false;
        return true;
    }
}
