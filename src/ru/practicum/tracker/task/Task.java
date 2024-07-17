package ru.practicum.tracker.task;

import java.util.Objects;

public class Task {
    private Integer uniqueID;
    private String name;
    private String description;
    private TaskState status;


    public Task(String name, String description, TaskState status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer uniqueID, String name, String description, TaskState status) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(Integer uniqueID) {
        this.uniqueID = uniqueID;
    }

    public TaskState getStatus() {
        return status;
    }

    public void setStatus(TaskState status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "uniqueID=" + uniqueID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(getUniqueID(), task.getUniqueID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueID(), getName(), getDescription(), getStatus());
    }
}
