import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.IntersectedTaskException;
import handlers.BaseHttpHandler;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

public class HttpTaskManagerEpicsTest {
    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager manager = taskServer.getManager();
    ;
    Gson gson = BaseHttpHandler.getJsonWithAdapters();
    int epicID = 0;

    @BeforeEach
    public void setUp() throws IOException, IntersectedTaskException {
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);
        //добавляем задачи
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        Epic epic2 = new Epic("epic2", "epic2 description", NEW);
        Epic epic3 = new Epic("epic3", "epic3 description", NEW);


        manager.addEpic(epic1);
        epicID = manager.addEpic(epic2);
        manager.addEpic(epic3);
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    // /epics GET
    @Test
    public void getEpicsTest() throws IntersectedTaskException, IOException, InterruptedException {

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        List<Task> epics = gson.fromJson(response.body(), new HttpTaskManagerEpicsTest.EpicListTypeToken().getType());
        assertEquals(manager.getAllEpic(), epics);
    }

    class EpicListTypeToken extends TypeToken<List<Epic>> {

    }

    // epics/{id} GET
    @Test
    public void getEpicByIDTest() throws IntersectedTaskException, IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Epic recievedEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals(epicID, recievedEpic.getTaskID());
        assertEquals(manager.getEpicByID(epicID).getName(), recievedEpic.getName());

    }

    // /epics POST createEpic()
    @Test
    public void createEpicTest() throws IOException, InterruptedException {
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);
        manager.clearEpics();
        // создаём задачу
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        // конвертируем её в JSON

        String epicJson = gson.toJson(epic1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epicsFromManager = manager.getAllEpic();
        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("epic1", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    // /epics POST updateEpic()
    @Test
    public void updateEpicTest() throws IOException, InterruptedException {
        Epic epic = manager.getEpicByID(epicID);
        epic.setName("Changed name");
        String taskJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());
        assertEquals("Changed name", manager.getEpicByID(epicID).getName());

    }

    // /epics/{id} DELETE
    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertNull(manager.getEpicByID(epicID));
    }

    //***********************************Errors****************************************************
    @Test
    public void tryToGetNotExistedEpicTest() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + 500);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }
}
