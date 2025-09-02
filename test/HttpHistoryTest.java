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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static task.Status.NEW;

public class HttpHistoryTest {

    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager manager = taskServer.getManager();


    @BeforeEach
    public void setUP() throws IOException {
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    // /history
    @Test
    public void getHistoryTest() throws IntersectedTaskException, IOException, InterruptedException {

        Task task = new Task("Task ", "Task description", NEW);
        manager.addTask(task);
        //создаем и добавляем эпик
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        manager.addEpic(epic);
        //создаем и добавляем две подзадачи
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        //добавляем в историю задачи по очереди
        int[] correct = {1, 2, 3, 4};
        getTaskByID(task.getTaskID());
        getTaskByID(task.getTaskID());
        getEpicByID(epic.getTaskID());
        getSubtaskByID(subtask1.getTaskID());
        getSubtaskByID(subtask2.getTaskID());
        assertArrayEquals(correct, getIDArray());
        int[] correct2 = {2, 3, 4, 1};
        getTaskByID(task.getTaskID());
        assertArrayEquals(correct2, getIDArray());
        int[] correct3 = {2, 4, 1, 3};
        getSubtaskByID(subtask1.getTaskID());
        assertArrayEquals(correct3, getIDArray());
    }

    private int[] getIDArray() throws IOException, InterruptedException {
        List<Task> history = getHistory();
        int size = history.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = history.get(i).getTaskID();
        }
        return array;
    }

    private List<Task> getHistory() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        Gson gson = BaseHttpHandler.getJsonWithAdapters();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        return gson.fromJson(response.body(), new HttpHistoryTest.TaskListTypeToken().getType());
    }

    class TaskListTypeToken extends TypeToken<List<Task>> {

    }

    private void getTaskByID(int taskID) throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
    }

    private void getEpicByID(int epicID) throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
    }

    private void getSubtaskByID(int subtaskID) throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
    }
}
