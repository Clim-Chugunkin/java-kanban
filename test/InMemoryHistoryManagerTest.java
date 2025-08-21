import exceptions.IntersectedTaskException;
import exceptions.ManagerSaveException;
import manager.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryHistoryManagerTest {

    private HistoryManager manager;

    @BeforeEach
    public void setUP() throws IOException, ManagerSaveException, IntersectedTaskException {
        manager = Managers.getDefaultHistory();
    }

    @Test
    public void addAndRemoveTaskTest() {
        Task task1 = new Task("task1", "task1 description", NEW);
        Task task2 = new Task("task2", "task2 description", NEW);
        task1.setTaskID(1);
        task2.setTaskID(2);
        //добавляем задачи в историю
        manager.addTask(task1);
        manager.addTask(task2);
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));
        //добавляем уже существующую задачу в историю
        manager.addTask(task1);
        //проверяем что задача сместилась в очереди
        assertEquals(task2, manager.getHistory().get(0));
        assertEquals(task1, manager.getHistory().get(1));
        //добавляем еще 9 задач, первая должна удалиться
        Task task = null;
        for (int i = 3; i < 3 + 9; i++) {
            task = new Task("task" + i, "task description", NEW);
            task.setTaskID(i);
            manager.addTask(task);
        }
        assertEquals(task2, manager.getHistory().getFirst());
        // Task remove
        manager.remove(task2.getTaskID());
        assertFalse(manager.getHistory().contains(task2));
    }
}
