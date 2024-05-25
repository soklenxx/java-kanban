package ru.practicum.task_tracker.task;


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

    public Integer getEpicID() {
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
}
