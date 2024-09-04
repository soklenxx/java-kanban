package ru.practicum.tracker.http.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.tracker.http.HttpTaskServer;
import ru.practicum.tracker.manager.HistoryManager;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.task.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            List<Task> history = taskManager.getHistory();
            String response = HttpTaskServer.getGson().toJson(history);
            sendText(exchange, response, 200);
        } else {
            sendText(exchange, "Некорректный запрос", 400);
        }
    }
}
