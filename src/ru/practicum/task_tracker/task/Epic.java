package ru.practicum.task_tracker.task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIDs = new ArrayList<>();

    public Epic(String name, String description, TaskState status) {
        super(name, description, status);
    }

    public Epic(Integer uniqueID, String name, String description, TaskState status) {
        super(uniqueID, name, description, status);
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
