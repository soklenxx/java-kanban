package ru.practicum.tracker.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIDs = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, TaskState status) {
        super(name, description, status);
    }

    public Epic(Integer uniqueID, String name, String description, TaskState status) {
        super(uniqueID, name, description, status);
    }

    public Epic(Integer uniqueID, String name, String description, TaskState status, int duration, LocalDateTime startTime) {
        super(uniqueID, name, description, status, duration, startTime);
        this.endTime = getEndTime();
    }

    public Epic(String name, String description, TaskState status, int duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.endTime = getEndTime();
    }

    public ArrayList<Integer> getSubtasksID() {
        return subtasksIDs;
    }

    public void addSubtaskIDs(int subtaskID) {
        subtasksIDs.add(subtaskID);
    }

    public void clearSubtaskIDs() {
        subtasksIDs.clear();
    }

    public void setEndTime(LocalDateTime endTime){ this.endTime = endTime; }

    @Override
    public LocalDateTime getEndTime(){ return this.getStartTime().plusMinutes(getDuration()); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUniqueID(), epic.getUniqueID());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "uniqueID=" +  getUniqueID() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasksIDs=" + subtasksIDs +
                '}';
    }
}
