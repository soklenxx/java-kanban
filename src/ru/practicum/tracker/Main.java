package ru.practicum.tracker;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.task.Epic;
import ru.practicum.tracker.task.TaskState;
import ru.practicum.tracker.task.Subtask;
import ru.practicum.tracker.task.Task;

import java.util.ArrayList;
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

//      Обновление задач
        Task task3 = new Task(task1.getUniqueID(), "Задача 2", "Description 2-2", TaskState.IN_PROGRESS);
        Task task1Update = taskManager.updateTask(task3);
        System.out.println(task1Update);

        Subtask subtask5 = new Subtask(subtask1.getUniqueID(), "Подзадача 2-2-2", "Description 2", TaskState.DONE, epic1.getUniqueID());
        Task subtask1Update = taskManager.updateSubtask(subtask5);
        System.out.println(subtask1Update);

        System.out.println(taskManager.getSubtaskByID(subtask1.getUniqueID()));


        System.out.println(taskManager.getEpicByID(2));
        System.out.println();
        System.out.println(taskManager.getTaskByID(task1.getUniqueID()));

//      Удаление задач
        taskManager.deleteTask(task1.getUniqueID());
        taskManager.deleteEpic(epic2.getUniqueID());
        System.out.println(taskManager.getEpicByID(epic2.getUniqueID()));
        System.out.println(taskManager.getTaskByID(task1.getUniqueID()));
        System.out.println(taskManager.getSubtaskByID(subtask4.getUniqueID()));

//      Получение списка подзадач епика
        ArrayList<Subtask> subtasksByEpic = taskManager.getSubtaskListByEpic(epic1);
        System.out.println(subtasksByEpic);

//      Удаление всех задач
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtask();
        taskManager.deleteAllEpics();


        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        taskManager.getEpicByID(epic2.getUniqueID());
        taskManager.getEpicByID(epic1.getUniqueID());
        taskManager.getTaskByID(task2.getUniqueID());
        taskManager.getTaskByID(task1.getUniqueID());
        taskManager.getTaskByID(task2.getUniqueID());
        taskManager.getEpicByID(epic2.getUniqueID());
        taskManager.getEpicByID(epic1.getUniqueID());
        taskManager.getTaskByID(task2.getUniqueID());
        taskManager.getTaskByID(task1.getUniqueID());
        taskManager.getTaskByID(task2.getUniqueID());
        taskManager.getTaskByID(task2.getUniqueID());
        taskManager.getSubtaskByID(subtask1.getUniqueID());
        List<Task> h = taskManager.getHistory();

        System.out.println(h);




    }
}
