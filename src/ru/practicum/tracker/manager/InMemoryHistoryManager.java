package ru.practicum.tracker.manager;

import ru.practicum.tracker.task.Task;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyList.size() >= 10) {
                historyList.removeFirst();
            }
        historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
