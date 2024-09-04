package ru.practicum.tracker.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private Integer uniqueID;
    private String name;
    private String description;
    private TaskState status;
    private TaskType type;
    private int duration;
    private LocalDateTime startTime;


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

    public Task(Integer uniqueID, String name, String description, TaskState status, int duration, LocalDateTime startTime) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, TaskState status, int duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(Duration.ofMinutes(duration));
        }
        return LocalDateTime.MAX;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Integer getEpicId() {
        return null;
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
