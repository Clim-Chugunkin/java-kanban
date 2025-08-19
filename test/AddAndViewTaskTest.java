import manager.IntersectedTaskException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

public class AddAndViewTaskTest {
    TaskManager manager;
    Task task;

    @BeforeEach
    void setUP() {
        manager = Managers.getInMemoryTaskManager();
        task = new Task("Test addNewTask", "Test addNewTask description", NEW);
    }

    @Test
    void AddNewTaskTest() throws IntersectedTaskException {

        final int taskID = manager.addTask(task);

        final Task savedTask = manager.getTaskByID(taskID);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void viewTask() throws IntersectedTaskException {
        int taskID = manager.addTask(task);
        Task task = manager.getTaskByID(taskID);

        String taskView = "Task{name='Test addNewTask', description='Test addNewTask description', taskID=1, status=NEW, startTime =null, duration = null}";
        assertEquals(taskView, task.toString(), "Неправильный вывод задачи");
    }

    //тест на проверку неизменности данных задачи
    @Test
    void checkTaskImmutability() throws IntersectedTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = manager.addTask(task);
        Task savedTask = manager.getTaskByID(taskId);

        assertEquals("Test addNewTask", savedTask.getName());
        assertEquals("Test addNewTask description", savedTask.getDescription());
        assertEquals(NEW, savedTask.getStatus());
    }

}
