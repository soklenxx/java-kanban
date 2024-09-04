package ru.practicum.tracker.http.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.tracker.http.HttpTaskServer;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.manager.exception.TaskTimeException;
import ru.practicum.tracker.task.Epic;
import ru.practicum.tracker.task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public EpicsHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Integer id = getIdFromPath(path);
        String[] splitPath = path.split("/");

        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == null) {
                    List<Epic> epics = taskManager.getEpics();
                    String response = HttpTaskServer.getGson().toJson(epics);
                    sendText(exchange, response, 200);
                } else if (splitPath.length >= 4) {
                    Epic epic = taskManager.getEpicByID(id);
                    List<Subtask> epicSubtasks = taskManager.getSubtaskListByEpic(epic);
                    if (epic == null) {
                        sendText(exchange, "Задача с id=" + id + " не существует", 404);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(epicSubtasks);
                        sendText(exchange, response, 200);
                    }
                } else {
                    Epic epic = taskManager.getEpicByID(id);
                    if (epic == null) {
                        sendText(exchange, "Задача с id=" + id + " не существует", 404);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(epic);
                        sendText(exchange, response, 200);
                    }
                }
                break;
            case "POST":
                if (id == null) {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
                    try {
                        taskManager.createEpic(epic);
                        Integer ids = epic.getUniqueID();
                        String response = "Задача с id=" + ids + " создана";
                        sendText(exchange, response, 201);
                    } catch (TaskTimeException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                } else {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
                    try {
                        taskManager.updateEpic(epic);
                        String response = "Задача с id=" + id + " обновлена";
                        sendText(exchange, response, 201);
                    } catch (TaskTimeException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                }
                break;
            case "DELETE":
                if (id == null) {
                    taskManager.deleteAllEpics();
                    String response = HttpTaskServer.getGson().toJson("Задачи удалены");
                    sendText(exchange, response, 200);
                } else {
                    taskManager.deleteEpic(id);
                    String response = HttpTaskServer.getGson().toJson("Задача удалена");
                    sendText(exchange, response, 200);
                }
                break;
            default:
                sendText(exchange, "Некорректный запрос", 400);
        }
    }
}
