package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.TaskState;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int generatorId;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Task createTask(Task task) {
        task.setUniqueID(getNewId());
        tasks.put(task.getUniqueID(), task);
        return task;
    }

    public Task updateTask(Task task) {
        Integer taskId = task.getUniqueID();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.put(taskId, task);
        return task;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTaskByID(int taskId) {
        return tasks.get(taskId);
    }

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Epic createEpic(Epic epic) {
        epic.setUniqueID(getNewId());
        epics.put(epic.getUniqueID(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getUniqueID();
        if (epicId == null || !epics.containsKey(epicId)) {
            return null;
        }
        epics.put(epicId, epic);
        return epic;
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpicByID(int epicId) {
        return epics.get(epicId);
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteEpic(int epicId) {
        ArrayList<Integer> subtaskIDs = getEpicByID(epicId).getSubtasksID();
        epics.remove(epicId);
        for (Integer subtask: subtaskIDs) {
            subtasks.remove(subtask);
        }
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setUniqueID(getNewId());
        subtasks.put(subtask.getUniqueID(), subtask);
        Epic epic = epics.get(subtask.getEpicID());
        if (epic != null) {
            epic.addSubtaskIds(subtask.getUniqueID());
            checkEpicStatus(epic);
        }

        return subtask;
    }

    public Subtask updateSubtask(Subtask subtask) {
        Integer subtaskId = subtask.getUniqueID();
        Epic epic = epics.get(subtask.getEpicID());
        if (subtaskId == null || !subtasks.containsKey(subtaskId)) {
            return null;
        }
        subtasks.put(subtaskId, subtask);
        checkEpicStatus(epic);
        return subtask;
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Subtask getSubtaskByID(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void deleteSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void deleteSubtaskID() {
        ArrayList<Integer> deleteEpicsIdList = new ArrayList<>(epics.keySet());
        for (Integer deleteId : deleteEpicsIdList) {
            Epic epic = getEpicByID(deleteId);
            epic.clearSubtaskIds();
        }

    }
    public void deleteAllSubtask() {
        subtasks.clear();
        deleteSubtaskID();
    }

    public ArrayList<Subtask> getSubtaskListByEpicID (Epic epic) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        ArrayList<Integer> subtasksIDs = epic.getSubtasksID();
        for (Integer id: subtasksIDs) {
            subtasks.add(getSubtaskByID(id));
        }
        return subtasks;
    }


    public void checkEpicStatus (Epic epic) {
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
}
