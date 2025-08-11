import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest {
    static TaskManager taskManager;

    @BeforeAll
    public static void before() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void writeToFileTest() {
        //Создайте две задачи, а также эпик с двумя подзадачами и  эпик с одной подзадачей.
        Task task = new Task("task1 name", "task1 description", Status.NEW);
        taskManager.addTask(task);
        Task task2 = new Task("task2 name", "task2 description", Status.IN_PROGRESS);
        taskManager.addTask(task2);


        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);

        Epic epic2 = new Epic("epic2 name", "epic2 description", Status.NEW);
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("subtask1 epic2 name", "subtask1 epic2 description", Status.IN_PROGRESS, epic2.getTaskID());
        taskManager.addSubTask(subtask3);
    }

    @Test
    public void readFromFileTest() {
        System.out.println("*******************TASKs**********************");
        System.out.println(taskManager.getAllTask());

        System.out.println("*******************EPICs********************");
        System.out.println(taskManager.getAllEpic());

        System.out.println("******************SUBTASKs*******************");
        System.out.println(taskManager.getAllSubTask());
        System.out.println("******************SUBTASKs from EPIC3*******************");
        System.out.println(taskManager.getAllEpicSubtask(3));
        Subtask subtask3 = new Subtask("subtask3 epic2 name", "subtask3 epic2 description", Status.IN_PROGRESS, 3);
        taskManager.addSubTask(subtask3);
        System.out.println("*****************SUBTASK AFTER ONE WAS ADDED**********************");
        System.out.println(taskManager.getAllEpicSubtask(3));
    }

}