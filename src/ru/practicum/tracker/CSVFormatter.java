package ru.practicum.tracker;

import ru.practicum.tracker.task.Task;
import ru.practicum.tracker.task.Epic;
import ru.practicum.tracker.task.Subtask;
import ru.practicum.tracker.task.TaskState;

public class CSVFormatter {
    public static String toString(Task task) {
        return new StringBuilder()
                .append(task.getUniqueID()).append(",")
                .append(task.getType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getEpicId()).append(",")
                .toString();
    }

    public static Task fromString(String scvRow) {
        String[] values = scvRow.split(",");
        String id = values[0];
        String type = values[1];
        String name = values[2];
        String status = values[3];
        String description = values[4];
        String epicId = values[5];
        return switch (type) {
            case "TASK" -> new Task(Integer.parseInt(id), name, description, TaskState.valueOf(status.toUpperCase()));
            case "EPIC" -> new Epic(Integer.parseInt(id), name, description, TaskState.valueOf(status.toUpperCase()));
            case "SUBTASK" ->
                    new Subtask(Integer.parseInt(id), name, description, TaskState.valueOf(status.toUpperCase()),
                            Integer.parseInt(epicId));
            default -> null;
        };
    }

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }
}
