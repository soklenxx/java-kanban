package ru.practicum.tracker.http.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.tracker.http.HttpTaskServer;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.task.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            Set<Task> prioritized  = taskManager.getPrioritizedTasks();
            String response = HttpTaskServer.getGson().toJson(prioritized);
            sendText(exchange, response, 200);
        } else {
            sendText(exchange, "Некорректный запрос", 400);
        }
    }
}
