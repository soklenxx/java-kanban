package ru.practicum.tracker;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.task.Epic;
import ru.practicum.tracker.task.TaskState;
import ru.practicum.tracker.task.Subtask;
import ru.practicum.tracker.task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        testTasks();
    }

    private static void testTasks() {
        TaskManager taskManager = Managers.getDefault();

        List<Task> tasks = taskManager.getTasks();
        System.out.println(tasks.isEmpty());

//      Создание задач
        Task task1 = new Task("Задача 1", "Description 1", TaskState.NEW);
        taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Description 2", TaskState.NEW);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Description 1", TaskState.NEW);
        taskManager.createEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Description 2", TaskState.NEW);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Description 1", TaskState.NEW, epic1.getUniqueID());
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Description 2", TaskState.NEW, epic1.getUniqueID());
        taskManager.createSubtask(subtask2);

        Subtask subtask3 = new Subtask("Подзадача 1-1", "Description 1", TaskState.NEW, epic2.getUniqueID());
        taskManager.createSubtask(subtask3);

        Subtask subtask4 = new Subtask("Подзадача 2-2", "Description 2", TaskState.NEW, epic2.getUniqueID());
        taskManager.createSubtask(subtask4);

//      Вывод всех задач
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println();

        taskManager.getEpicByID(epic2.getUniqueID());
        taskManager.getEpicByID(epic2.getUniqueID());
        taskManager.getSubtaskByID(subtask1.getUniqueID());
        List<Task> h = taskManager.getHistory();

        System.out.println(h);
        System.out.println(h);

    }
}
