import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest {
    private static String fileName = "";

    @BeforeAll
    public static void beforeTests() {
        try {
            File f = File.createTempFile("out", ".txt");
            fileName = f.getName();
        } catch (IOException ex) {
            System.out.println("Фаил не может быть создан");
        }
    }

    @Test
    public void writeToFileTest() {
        FileBackedTaskManager manager = new FileBackedTaskManager(fileName);
        //Создайте две задачи, а также эпик с двумя подзадачами и  эпик с одной подзадачей.
        Task task = new Task("task1 name", "task1 description", Status.NEW);
        manager.addTask(task);
        Task task2 = new Task("task2 name", "task2 description", Status.IN_PROGRESS);
        manager.addTask(task2);


        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        Epic epic2 = new Epic("epic2 name", "epic2 description", Status.NEW);
        manager.addEpic(epic2);
        Subtask subtask3 = new Subtask("subtask1 epic2 name", "subtask1 epic2 description", Status.IN_PROGRESS, epic2.getTaskID());
        manager.addSubTask(subtask3);
    }

    @Test
    public void readFromFileTest() {
        FileBackedTaskManager manager = new FileBackedTaskManager(fileName);
        try {
            manager.loadFromFile();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("*******************TASKs**********************");
        System.out.println(manager.getAllTask());

        System.out.println("*******************EPICs********************");
        System.out.println(manager.getAllEpic());

        System.out.println("******************SUBTASKs*******************");
        System.out.println(manager.getAllSubTask());
        System.out.println("******************SUBTASKs from EPIC3*******************");
        System.out.println(manager.getAllEpicSubtask(3));
        Subtask subtask3 = new Subtask("subtask3 epic2 name", "subtask3 epic2 description", Status.IN_PROGRESS, 3);
        manager.addSubTask(subtask3);
        System.out.println("*****************SUBTASK AFTER ONE WAS ADDED**********************");
        System.out.println(manager.getAllEpicSubtask(3));
    }

}