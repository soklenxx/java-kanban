package ru.practicum.tracker.manager;

import ru.practicum.tracker.CSVFormatter;
import ru.practicum.tracker.ManagerSaveException;
import ru.practicum.tracker.task.Epic;
import ru.practicum.tracker.task.Subtask;
import ru.practicum.tracker.task.Task;
import ru.practicum.tracker.task.TaskType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public static String PATH_TO_FILE = "src/resources/data.csv";
    public Path file;

    public FileBackedTaskManager(Path file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(Path.of(PATH_TO_FILE));
            int maxId = 0;
            for (int i = 1; i < lines.size(); i++) {
                Task task = CSVFormatter.fromString(lines.get(i));
                if (task.getType() == TaskType.TASK) {
                    fileBackedTaskManager.createTask(task);
                } else if (task.getType() == TaskType.EPIC) {
                    fileBackedTaskManager.createEpic((Epic) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    fileBackedTaskManager.createSubtask((Subtask) task);
                }
                if (maxId < task.getUniqueID()) {
                    maxId = task.getUniqueID();
                }
            }
            fileBackedTaskManager.setGeneratorId(maxId);
        } catch (IOException e) {
            throw ManagerSaveException.loadException(e);
        }
        return fileBackedTaskManager;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask =  super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        save();
        return updatedEpic;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtask(subtask);
        save();
        return updatedSubtask;
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_TO_FILE))) {
            bw.write(CSVFormatter.getHeader());
            bw.newLine();
            for (Task task : getTasks()) {
                bw.write(CSVFormatter.toString(task));
                bw.newLine();
            }
            for (Epic epic : getEpics()) {
                bw.write(CSVFormatter.toString(epic));
                bw.newLine();
            }
            for (Subtask subtask : getSubtasks()) {
                bw.write(CSVFormatter.toString(subtask));
                bw.newLine();
            }
        } catch (IOException e) {
            throw ManagerSaveException.saveException(e);
        }

    }
}
