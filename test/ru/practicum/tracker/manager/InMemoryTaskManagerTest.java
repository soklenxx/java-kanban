package ru.practicum.tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tracker.Managers;
import ru.practicum.tracker.task.Epic;
import ru.practicum.tracker.task.Subtask;
import ru.practicum.tracker.task.Task;
import ru.practicum.tracker.task.TaskState;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;

    @BeforeEach
    void beforeEach() {
        task1 = new Task(0,"Задача 1", "Description 1", TaskState.NEW);
        task2 = new Task(0,"Задача 3", "Description 3", TaskState.NEW);

        epic1 = new Epic(3,"Эпик 1", "Description 1", TaskState.NEW);
        epic2 = new Epic(3,"Эпик 3", "Description 3", TaskState.NEW);

        subtask1 = new Subtask(0,"Сабтаск 1", "Description 3", TaskState.NEW, 3);
    }
    @Test
    public void shouldReturnEqualsTasks() {
        assertEquals(task1, task2);
    }
    @Test
    public void shouldReturnEqualsEpics() {
        assertEquals(epic1, epic2);
    }

    @Test
    public void utilityClassAlwaysReturnsInitializedReadyToUseManagerInstances() {
        assertNotNull(taskManager);
        assertNotNull(historyManager);
    }
    @Test
    public void actuallyAddsTasksOfDifferentTypesInMemoryTaskManager() {
        TaskManager taskManagerExpected = Managers.getDefault();
        task1 = new Task(1,"Задача 1", "Description 1", TaskState.NEW);
        task2 = new Task(1,"Задача 3", "Description 3", TaskState.NEW);
        epic1 = new Epic(3,"Эпик 1", "Description 1", TaskState.NEW);
        epic2 = new Epic(3,"Эпик 3", "Description 3", TaskState.NEW);

        subtask1 = new Subtask(0,"Сабтаск 1", "Description 3", TaskState.NEW, 3);

        assertEquals(taskManagerExpected.getTasks(), taskManager.getTasks());
        assertEquals(taskManagerExpected.getSubtasks(), taskManager.getSubtasks());
        assertEquals(taskManagerExpected.getEpics(), taskManager.getEpics());
    }

    @Test
    public void tasksWithGivenIdAndGeneratedIdDoNotConflictWithinManager() {
        TaskManager taskManagerExpected = Managers.getDefault();
        Subtask subtask2 = new Subtask("Сабтаск 1", "Description 3", TaskState.NEW, 3);
        taskManagerExpected.createSubtask(subtask2);
        assertEquals(taskManagerExpected.getSubtaskByID(0), subtask1);
    }

    @Test
    public void taskRemainsUnchangedWhenAddingTaskToManager() {
        taskManager.createTask(task1);
        assertEquals(taskManager.getTaskByID(0), new Task(0,"Задача 1", "Description 1", TaskState.NEW));
    }

    @Test
    public void tasksAddedToHistoryManagerRetainThePreviousVersionOfTaskAndItsData() {
        taskManager.createTask(task1);
        taskManager.getTaskByID(0);
        assertEquals(taskManager.getTasks(),taskManager.getHistory());
    }
}