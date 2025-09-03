import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.IntersectedTaskException;
import handlers.BaseHttpHandler;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static task.Status.NEW;

public class HttpTaskManagerSubtasksTest {
    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager manager = taskServer.getManager();
    Gson gson = BaseHttpHandler.getJsonWithAdapters();
    int subtaskID = 0;
    int epicID = 0;

    @BeforeEach
    public void setUp() throws IOException, IntersectedTaskException {
        LocalDateTime time = LocalDateTime.of(2020, 8, 1, 10, 0, 0);
        //добавляем задачи
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        epicID = manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2), epicID);

        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.IN_PROGRESS,
                time.plus(Duration.ofMinutes(25)),
                Duration.ofMinutes(2), epicID);

        manager.addSubTask(subtask1);
        subtaskID = manager.addSubTask(subtask2);
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    // /subtasks GET
    @Test
    public void getSubtasksTest() throws IntersectedTaskException, IOException, InterruptedException {

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        List<Task> epics = gson.fromJson(response.body(), new HttpTaskManagerSubtasksTest.SubtasksListTypeToken().getType());
        assertEquals(manager.getAllSubTask(), epics);
    }

    class SubtasksListTypeToken extends TypeToken<List<Subtask>> {

    }

    // subatasks/{id} GET
    @Test
    public void getSubtaskByIDTest() throws IntersectedTaskException, IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Subtask recievedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtaskID, recievedSubtask.getTaskID());
        assertEquals(manager.getSubtaskByID(subtaskID).getName(), recievedSubtask.getName());

    }

    // /subtasks POST createEpic()
    @Test
    public void createSubtaskTest() throws IOException, InterruptedException {
        LocalDateTime time = LocalDateTime.of(2023, 8, 1, 10, 0, 0);
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        int epicID = manager.addEpic(epic1);
        manager.clearSubtask();
        // создаём задачу
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2), epicID);
        // конвертируем её в JSON

        String epicJson = gson.toJson(subtask1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> subtaskFromManager = manager.getAllSubTask();
        assertNotNull(subtaskFromManager, "Задачи не возвращаются");
        assertEquals(1, subtaskFromManager.size(), "Некорректное количество задач");
        assertEquals("subtask1", subtaskFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    // /subtask POST updateSubtask()
    @Test
    public void updateSubtaskTest() throws IOException, InterruptedException {
        Subtask subtask = manager.getSubtaskByID(subtaskID);
        subtask.setName("Changed name");
        String taskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());
        assertEquals("Changed name", manager.getSubtaskByID(subtaskID).getName());

    }

    // /subtasks/{id} DELETE
    @Test
    public void deletesubtask() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertNull(manager.getSubtaskByID(subtaskID));
    }

    //***********************************Errors****************************************************
    @Test
    public void tryToGetNotExistedSubtaskTest() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/" + 500);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }

    @Test
    public void tryToAddIntersectedSubtask() throws IOException, InterruptedException {
        LocalDateTime time = LocalDateTime.of(2020, 8, 1, 10, 0, 0);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2), epicID);
        String epicJson = gson.toJson(subtask1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());
    }
}
