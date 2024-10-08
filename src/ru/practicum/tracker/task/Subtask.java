package ru.practicum.tracker.task;


import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Integer epicID;

    public Subtask(String name, String description, TaskState status, Integer epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public Subtask(Integer uniqueID, String name, String description, TaskState status, Integer epicID) {
        super(uniqueID, name, description, status);
        this.epicID = epicID;
    }

    public Subtask(Integer uniqueID, String name, String description, TaskState status, Integer epicID, int duration, LocalDateTime startTime) {
        super(uniqueID, name, description, status, duration, startTime);
        this.epicID = epicID;
    }

    public Subtask(String name, String description, TaskState status, Integer epicID, int duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicID = epicID;
    }

    @Override
    public Integer getEpicId() {
        return epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "uniqueID=" +  getUniqueID() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicID=" + epicID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask subtask)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUniqueID(), subtask.getUniqueID());
    }
}
