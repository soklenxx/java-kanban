package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    Task updateTask(Task task);

    List<Task> getTasks();

    Task getTaskByID(int taskId);

    void deleteTask(int taskId);

    void deleteAllTasks();

    Epic createEpic(Epic epic);

    Epic updateEpic(Epic epic);

    List<Epic> getEpics();

    Epic getEpicByID(int epicId);

    void deleteAllEpics();

    void deleteEpic(int epicId);

    Subtask createSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask);

    List<Subtask> getSubtasks();

    Subtask getSubtaskByID(int subtaskId);

    void deleteSubtask(int subtaskId);

    void deleteAllSubtask();

    ArrayList<Task> getHistory();

    ArrayList<Subtask> getSubtaskListByEpic(Epic epic);
}
