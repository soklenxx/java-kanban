package ru.practicum.tracker;

import ru.practicum.tracker.manager.HistoryManager;
import ru.practicum.tracker.manager.InMemoryHistoryManager;
import ru.practicum.tracker.manager.InMemoryTaskManager;
import ru.practicum.tracker.manager.TaskManager;

public class Managers {

    private Managers() {}
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() { return new InMemoryHistoryManager(); }
}
