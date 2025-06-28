
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import task.Task;


import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

public class EraseTaskTest {
    TaskManager manager;
    Task task;


    @BeforeEach
    void setUP() {
        manager = Managers.getDefault();
        task = new Task("Test addNewTask", "Test addNewTask description", NEW);
    }

    @Test
    void eraseOneTask() {
        //добавляем две задачи
        int taskID = manager.addTask(task);
        Task task = manager.getTaskByID(taskID);
        Task task2 = new Task("task2", "task2 description", NEW);
        manager.addTask(task2);

        //проверяем количество задач
        assertEquals(2, manager.getAllTask().size());

        //удаляем одну задачу и проверяем количество задач

        manager.eraseTaskByID(taskID);
        assertEquals(1, manager.getAllTask().size());
    }

    @Test
    void eraseAllTasks() {
        //добавляем две задачи
        int taskID = manager.addTask(task);
        Task task = manager.getTaskByID(taskID);
        Task task2 = new Task("task2", "task2 description", NEW);
        manager.addTask(task2);

        //удаляем все задачи и проверяем

        manager.clearTasks();

        assertEquals(0, manager.getAllTask().size());

    }

}
