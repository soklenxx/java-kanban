package ru.practicum.tracker.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.tracker.http.HttpTaskServer;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.manager.exception.TaskTimeException;
import ru.practicum.tracker.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Processes /tasks
 */

public class TasksHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public TasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id==null) {
                    List<Task> tasks = taskManager.getTasks();
                    String response = HttpTaskServer.getGson().toJson(tasks);
                    sendText(exchange, response, 200);
                } else {
                    Task task = taskManager.getTaskByID(id);
                    if (task == null) {
                        sendText(exchange, "Задача с id=" + id + " не существует", 404);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(task);
                        sendText(exchange, response, 200);
                    }
                }
                break;
            case "POST":
                if (id==null) {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
                    try {
                        taskManager.createTask(task);
                        Integer ids = task.getUniqueID();
                        String response = "Задача с id=" + ids + " создана";
                        sendText(exchange, response, 201);
                    } catch (TaskTimeException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                } else {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
                    try {
                        taskManager.updateTask(task);
                        String response = "Задача с id=" + id + " обновлена";
                        sendText(exchange, response, 201);
                    } catch (TaskTimeException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                }
                break;
            case "DELETE":
                if (id==null) {
                    taskManager.deleteAllTasks();
                    String response = HttpTaskServer.getGson().toJson("Задачи удалены");
                    sendText(exchange, response, 200);
                } else {
                    taskManager.deleteTask(id);
                    String response = HttpTaskServer.getGson().toJson("Задача удалена");
                    sendText(exchange, response, 200);
                }
                break;
            default:
                sendText(exchange, "Некорректный запрос", 400);
        }
    }
}
