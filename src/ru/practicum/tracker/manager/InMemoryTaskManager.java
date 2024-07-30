package ru.practicum.tracker.manager;

import ru.practicum.tracker.Managers;
import ru.practicum.tracker.task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int generatorId;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task createTask(Task task) {
        if (task.getUniqueID() == null) {
            task.setUniqueID(getNewId());
        }
        task.setType(TaskType.TASK);
        tasks.put(task.getUniqueID(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getUniqueID();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskByID(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic.getUniqueID() == null) {
            epic.setUniqueID(getNewId());
        }
        epic.setType(TaskType.EPIC);
        epics.put(epic.getUniqueID(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getUniqueID();
        if (epicId == null || !epics.containsKey(epicId)) {
            return null;
        }
        epics.put(epicId, epic);
        return epic;
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicByID(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteEpic(int epicId) {
        ArrayList<Integer> subtaskIDs = getEpicByID(epicId).getSubtasksID();
        epics.remove(epicId);
        for (Integer subtask: subtaskIDs) {
            subtasks.remove(subtask);
        }
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask.getUniqueID() == null) {
            subtask.setUniqueID(getNewId());
        }
        subtask.setType(TaskType.SUBTASK);
        subtasks.put(subtask.getUniqueID(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskIDs(subtask.getUniqueID());
            checkEpicStatus(epic);
        }

        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Integer subtaskId = subtask.getUniqueID();
        Epic epic = epics.get(subtask.getEpicId());
        if (subtaskId == null || !subtasks.containsKey(subtaskId)) {
            return null;
        }
        subtasks.put(subtaskId, subtask);
        checkEpicStatus(epic);
        return subtask;
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskByID(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        Epic epic = getEpicByID(getSubtaskByID(subtaskId).getEpicId());
        ArrayList<Integer> subtaskIDs = epic.getSubtasksID();
        subtasks.remove(subtaskId);
        subtaskIDs.remove(subtaskId);
        checkEpicStatus(epic);
    }

    @Override
    public void deleteAllSubtask() {
        ArrayList<Integer> deleteEpicsIdList = new ArrayList<>(epics.keySet());
        subtasks.clear();
        for (Integer deleteId : deleteEpicsIdList) {
            Epic epic = getEpicByID(deleteId);
            epic.clearSubtaskIDs();
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskListByEpic(Epic epic) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        ArrayList<Integer> subtasksIDs = epic.getSubtasksID();
        for (Integer id: subtasksIDs) {
            subtasks.add(getSubtaskByID(id));
        }
        return subtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void checkEpicStatus (Epic epic) {
        if (epics.containsKey(epic.getUniqueID())) {
            if (epic.getSubtasksID().size() == 0) {
                epic.setStatus(TaskState.NEW);
            } else {
                int countDone = 0;
                int countNew = 0;

                for (Integer subtask: epic.getSubtasksID()) {
                    if (subtasks.get(subtask).getStatus() == TaskState.DONE) {
                        countDone++;
                    }
                    if (subtasks.get(subtask).getStatus() == TaskState.NEW) {
                        countNew++;
                    }
                }

                if (countNew == epic.getSubtasksID().size()) {
                    epic.setStatus(TaskState.NEW);
                } else if (countDone == epic.getSubtasksID().size()) {
                    epic.setStatus(TaskState.DONE);
                } else {
                    epic.setStatus(TaskState.IN_PROGRESS);
                }
            }
        }

    }

    private int getNewId() {
        return generatorId++;
    }

    public void setGeneratorId(int generatorId) {
        this.generatorId = generatorId;
    }
}
