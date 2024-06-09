package ru.practicum.task_tracker.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.Managers;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic1;
    TaskManager taskManager = Managers.getDefault();
    @Test
    public void crateEpic() {
        epic1 = new Epic("Эпик 1", "Description 1", TaskState.NEW);
        taskManager.createEpic(epic1);
        assertEquals(taskManager.getEpicByID(0), new Epic(0,"Эпик 1", "Description 1", TaskState.NEW));
    }
}