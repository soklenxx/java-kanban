package ru.practicum.tracker.manager;

import ru.practicum.tracker.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}
