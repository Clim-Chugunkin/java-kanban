import exceptions.IntersectedTaskException;
import exceptions.ManagerSaveException;
import manager.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private final LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);

    @BeforeEach
    public void setUP() throws IOException, ManagerSaveException, IntersectedTaskException {
        taskManager = FileBackedTaskManager.loadFromFile(Files.createTempFile("out", "txt").toFile());
    }

    @Test
    public void writeAndReadTaskFromFile() throws IOException, ManagerSaveException, IntersectedTaskException {
        //Пустой список задач.
        File file = Files.createTempFile("out", "txt").toFile();
        taskManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(0, taskManager.getAllTask().size());
        assertEquals(0, taskManager.getAllEpic().size());
        assertEquals(0, taskManager.getAllSubTask().size());
        //Пустой список истории
        assertEquals(0, taskManager.getHistory().size());

        //Эпик без подзадач.
        file = Files.createTempFile("out", "txt").toFile();
        taskManager = FileBackedTaskManager.loadFromFile(file);
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        //сохраняем эпик
        taskManager.addEpic(epic);
        //восстанавливаем
        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(epic, taskManager.getAllEpic().getFirst());


        file = Files.createTempFile("out", "txt").toFile();
        taskManager = FileBackedTaskManager.loadFromFile(file);
        //Создайте две задачи, а также эпик с двумя подзадачами и  эпик с одной подзадачей.
        Task task = new Task("task1 name", "task1 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2));
        taskManager.addTask(task);

        Task task2 = new Task("task2 name", "task2 description", Status.IN_PROGRESS,
                time.plus(Duration.ofMinutes(25)),
                Duration.ofMinutes(2));
        taskManager.addTask(task2);
        epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW,
                time.plus(Duration.ofMinutes(30)),
                Duration.ofMinutes(2),
                epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW,
                time.plus(Duration.ofMinutes(40)),
                Duration.ofMinutes(2),
                epic.getTaskID());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        Epic epic2 = new Epic("epic2 name", "epic2 description", Status.NEW);
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("subtask1 epic2 name", "subtask1 epic2 description", Status.IN_PROGRESS,
                time.plus(Duration.ofMinutes(50)),
                Duration.ofMinutes(2),
                epic2.getTaskID());
        taskManager.addSubTask(subtask3);
        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(task, taskManager.getAllTask().get(0));
        assertEquals(task2, taskManager.getAllTask().get(1));
        assertEquals(epic, taskManager.getAllEpic().getFirst());
        assertEquals(subtask1, taskManager.getAllSubTask().get(0));
        assertEquals(subtask2, taskManager.getAllSubTask().get(1));
    }
}