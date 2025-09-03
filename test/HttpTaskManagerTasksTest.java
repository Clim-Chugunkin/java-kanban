import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.IntersectedTaskException;
import handlers.BaseHttpHandler;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
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
import static task.Status.NEW;

public class HttpTaskManagerTasksTest {

    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager manager = taskServer.getManager();;
    Gson gson = BaseHttpHandler.getJsonWithAdapters();
    int taskID = 0;

    @BeforeEach
    public void setUp() throws IOException, IntersectedTaskException {
        manager.clearTasks();
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);
        //добавляем задачи
        Task task1 = new Task("task1", "task1 description", NEW);
        Task task2 = new Task("task2", "task2 description", NEW);
        Task task3 = new Task("task3", "task3 description", NEW);

        Task task4 = new Task("task4", "task4 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2));

        Task task5 = new Task("task5", "task5 description", Status.IN_PROGRESS,
                time.plus(Duration.ofMinutes(25)),
                Duration.ofMinutes(2));
        manager.addTask(task5);
        manager.addTask(task4);
        taskID = manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }
    // /tasks GET
    @Test
    public void getTasksTest() throws IntersectedTaskException, IOException, InterruptedException {

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(),new TaskListTypeToken().getType());
        assertEquals(manager.getAllTask(),tasks);
    }

    class TaskListTypeToken extends TypeToken<List<Task>> {

    }

    // /tasks/{id} GET
    @Test
    public void getTaskByIDTest() throws IntersectedTaskException, IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/"+ taskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Task recievedTask = gson.fromJson(response.body(),Task.class);
        assertEquals(taskID,recievedTask.getTaskID());
        assertEquals(manager.getTaskByID(taskID).getName(),recievedTask.getName());

    }

    // /tasks POST createTask()
    @Test
    public void createTaskTest() throws IOException, InterruptedException {
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);
        manager.clearTasks();
        // создаём задачу
        Task task = new Task("task", "task description", Status.NEW,
                time.plus(Duration.ofMinutes(50)),
                Duration.ofMinutes(2));
        // конвертируем её в JSON

        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTask();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("task", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    // /tasks POST updateTask()
    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        Task task = manager.getTaskByID(taskID);
        task.setName("Changed name");
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/"+taskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());
        assertEquals("Changed name",manager.getTaskByID(taskID).getName());

    }

    // /tasks/{id} DELETE
    @Test
    public void deleteTask() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/"+taskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertNull(manager.getTaskByID(taskID));
    }
    //***********************************Errors****************************************************
    @Test
    public void tryToGetNotExistedTaskTest() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/"+500);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }
    @Test
    public void tryToAddIntersectedTask() throws IOException, InterruptedException {
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);
        Task task = new Task("task4", "task4 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2));
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());
    }
}
