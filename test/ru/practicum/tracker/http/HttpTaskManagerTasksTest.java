package ru.practicum.tracker.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import ru.practicum.tracker.Managers;
import ru.practicum.tracker.manager.TaskManager;
import ru.practicum.tracker.task.Task;
import ru.practicum.tracker.task.TaskState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static java.net.http.HttpClient.newHttpClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskTaskManager
    TaskManager taskManager = Managers.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtask();
        taskManager.deleteAllEpics();
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2",
                TaskState.NEW, 5, LocalDateTime.now());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromTaskManager = taskManager.getTasks();

        assertNotNull(tasksFromTaskManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromTaskManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromTaskManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2",
                TaskState.NEW, 5, LocalDateTime.now());
        task = taskManager.createTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromTaskManager = taskManager.getTasks();

        assertNotNull(tasksFromTaskManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromTaskManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromTaskManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1,"Test 2", "Testing task 2",
                TaskState.NEW, 5, LocalDateTime.now());
        task = taskManager.createTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromTaskManager = taskManager.getTasks();

        assertNotNull(tasksFromTaskManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromTaskManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromTaskManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1,"Test 2", "Testing task 2",
                TaskState.NEW, 5, LocalDateTime.now());
        task = taskManager.createTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromTaskManager = taskManager.getTasks();

        assertNotNull(tasksFromTaskManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromTaskManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testDeleteTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1,"Test 2", "Testing task 2",
                TaskState.NEW, 5, LocalDateTime.now());
        task = taskManager.createTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromTaskManager = taskManager.getTasks();

        assertNotNull(tasksFromTaskManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromTaskManager.size(), "Некорректное количество задач");
    }
}