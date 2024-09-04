package ru.practicum.tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.tracker.Managers;
import ru.practicum.tracker.http.adapter.LocalDateTimeAdapter;
import ru.practicum.tracker.http.handler.*;
import ru.practicum.tracker.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpServer.createContext("/tasks", new TasksHttpHandler(taskManager));
        this.httpServer.createContext("/epics", new EpicsHttpHandler(taskManager));
        this.httpServer.createContext("/subtasks", new SubtasksHttpHandler(taskManager));
        this.httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
        this.httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager));
        this.taskManager = taskManager;
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
