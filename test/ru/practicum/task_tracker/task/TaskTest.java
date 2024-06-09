package ru.practicum.task_tracker.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.Managers;
import ru.practicum.task_tracker.manager.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task1;
    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        task1 = new Task("Задача 1", "Description 1", TaskState.NEW);
    }
    @Test
    public void crateTask() {
        task1 = new Task("Задача 1", "Description 1", TaskState.NEW);
        taskManager.createTask(task1);
        assertEquals(taskManager.getTaskByID(0), new Task(0,"Задача 1", "Description 1", TaskState.NEW));
    }

    @Test
    public void updateTask() {
        taskManager.createTask(task1);
        Task task3 = new Task(task1.getUniqueID(), "Задача 2", "Description 2-2", TaskState.IN_PROGRESS);
        taskManager.updateTask(task3);
        assertEquals("Description 2-2", taskManager.getTaskByID(0).getDescription());
    }

    @Test
    public void deleteTask() {
        taskManager.createTask(task1);
        taskManager.deleteTask(task1.getUniqueID());
        assertNull(taskManager.getTaskByID(0));
    }
}