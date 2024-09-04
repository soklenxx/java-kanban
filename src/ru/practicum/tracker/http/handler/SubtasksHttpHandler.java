package ru.practicum.tracker.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.tracker.http.HttpTaskServer;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.manager.exception.TaskTimeException;
import ru.practicum.tracker.task.Epic;
import ru.practicum.tracker.task.Subtask;
import ru.practicum.tracker.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public SubtasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id==null) {
                    List<Subtask> subtasks = taskManager.getSubtasks();
                    String response = HttpTaskServer.getGson().toJson(subtasks);
                    sendText(exchange, response, 200);
                } else {
                    Subtask subtask = taskManager.getSubtaskByID(id);
                    if (subtask == null) {
                        sendText(exchange, "Задача с id=" + id + " не существует", 404);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(subtask);
                        sendText(exchange, response, 200);
                    }
                }
                break;
            case "POST":
                if (id==null) {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = HttpTaskServer.getGson().fromJson(body, Subtask.class);
                    try {
                        taskManager.createSubtask(subtask);
                        Integer ids = subtask.getUniqueID();
                        String response = "Задача с id=" + ids + " создана";
                        sendText(exchange, response, 201);
                    } catch (TaskTimeException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                } else {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = HttpTaskServer.getGson().fromJson(body, Subtask.class);
                    try {
                        taskManager.updateSubtask(subtask);
                        String response = "Задача с id=" + id + " обновлена";
                        sendText(exchange, response, 201);
                    } catch (TaskTimeException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                }
                break;
            case "DELETE":
                if (id==null) {
                    taskManager.deleteAllSubtask();
                    String response = HttpTaskServer.getGson().toJson("Задачи удалены");
                    sendText(exchange, response, 200);
                } else {
                    taskManager.deleteSubtask(id);
                    String response = HttpTaskServer.getGson().toJson("Задача удалена");
                    sendText(exchange, response, 200);
                }
                break;
            default:
                sendText(exchange, "Некорректный запрос", 400);
        }
    }
}
