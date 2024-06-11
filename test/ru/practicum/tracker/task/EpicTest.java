package ru.practicum.tracker.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import ru.practicum.tracker.Managers;
import ru.practicum.tracker.manager.TaskManager;

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