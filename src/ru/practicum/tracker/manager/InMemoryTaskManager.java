package ru.practicum.tracker.manager;

import ru.practicum.tracker.Managers;
import ru.practicum.tracker.task.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int generatorId;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getUniqueID));

    @Override
    public Task createTask(Task task) {
        if (task.getUniqueID() == null) {
            task.setUniqueID(getNewId());
        }
        task.setType(TaskType.TASK);
        prioritizedTasks.add(task);
        validation(task);
        tasks.put(task.getUniqueID(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getUniqueID();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        prioritizedTasks.add(task);
        validation(task);
        prioritizedTasks.remove(tasks.get(taskId));
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
        prioritizedTasks.remove(tasks.get(taskId));
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
        prioritizedTasks.add(epic);
        findDurationAndStartTimeOfEpic(epic);
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
        prioritizedTasks.remove(epics.get(epicId));
        epics.remove(epicId);
        for (Integer subtask: subtaskIDs) {
            prioritizedTasks.remove(subtasks.get(subtask));
            subtasks.remove(subtask);
        }
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask.getUniqueID() == null) {
            subtask.setUniqueID(getNewId());
        }
        subtask.setType(TaskType.SUBTASK);
        prioritizedTasks.add(subtask);
        validation(subtask);
        subtasks.put(subtask.getUniqueID(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskIDs(subtask.getUniqueID());
            checkEpicStatus(epic);
            findDurationAndStartTimeOfEpic(epic);
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
        validation(subtask);
        prioritizedTasks.remove(epics.get(subtaskId));
        subtasks.put(subtaskId, subtask);
        prioritizedTasks.add(subtask);
        findDurationAndStartTimeOfEpic(epic);
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
        prioritizedTasks.remove(epics.get(subtaskId));
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
    public List<Subtask> getSubtaskListByEpic(Epic epic) {
        return epic.getSubtasksID().stream()
                .map(this::getSubtaskByID)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void findDurationAndStartTimeOfEpic(Epic epic) {
        LocalDateTime startTime = epic.getSubtasksID().stream()
                .map(subtasks::get)
                .map(Task::getStartTime)
                .reduce(null, (min, start) -> ((min == null) || (min.isAfter(start)) ? start : min));

        LocalDateTime endTime = epic.getSubtasksID().stream()
                .map(subtasks::get)
                .map(Task::getEndTime)
                .reduce(null, (max, end) -> ((max == null) || (max.isBefore(end)) ? end : max));

        int duration = epic.getSubtasksID().stream()
                .map(subtasks::get)
                .mapToInt(Task::getDuration)
                .sum();

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    @Override
    public void validation(Task task) {
        boolean taskHasIntersections = validationCheck(task);
        if (!taskHasIntersections) {
            System.out.println(task.getStartTime());
            throw new RuntimeException("Задача пересекается с уже существующей");
        }
    }

    @Override
    public boolean validationCheck(Task task) {
        for (Task taskCheck : prioritizedTasks) {
            if (taskCheck.getStartTime() != null && taskCheck.getEndTime() != null) {
                if (
                        (task.getEndTime().isBefore(taskCheck.getStartTime())
                                || task.getEndTime().equals(taskCheck.getStartTime())
                                || task.getStartTime().isAfter(taskCheck.getEndTime())
                                || task.getStartTime().equals(taskCheck.getStartTime()))
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkEpicStatus(Epic epic) {
        if (epics.containsKey(epic.getUniqueID())) {
            if (epic.getSubtasksID().isEmpty()) {
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
