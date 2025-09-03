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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpPrioritizedTest {

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

    @Test
    public void getPrioritizedTasksTest() throws IntersectedTaskException, IOException, InterruptedException {
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);

        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        int epicID = manager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2),
                epic.getTaskID());

        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW,
                time.plus(Duration.ofHours(1)),
                Duration.ofMinutes(5),
                epic.getTaskID());

        Subtask subtask3 = new Subtask("subtask3 epic1 name", "subtask3 epic1 description", Status.NEW, epic.getTaskID());

        Subtask subtask4 = new Subtask("subtask4 epic1 name", "subtask4 epic1 description", Status.NEW,
                time.plus(Duration.ofMinutes(10)),
                Duration.ofMinutes(5),
                epic.getTaskID());
        manager.addSubTask(subtask3);
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        manager.addSubTask(subtask4);
        Set<Subtask> tasks = getPrioritizedTasks();
        assertEquals(subtask4, tasks.toArray()[0]);
        assertEquals(subtask1, tasks.toArray()[1]);
        assertEquals(subtask2, tasks.toArray()[2]);
        assertEquals(subtask3, tasks.toArray()[3]);
    }

    private Set<Subtask> getPrioritizedTasks() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        Gson gson = BaseHttpHandler.getJsonWithAdapters();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        return gson.fromJson(response.body(), new SubtaskSetTypeToken().getType());

    }

    class SubtaskSetTypeToken extends TypeToken<Set<Subtask>> {

    }

}
