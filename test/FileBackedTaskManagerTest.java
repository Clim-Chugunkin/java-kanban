import manager.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest {

    static LocalDateTime time;

    @BeforeAll
    public static void before() {

        time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);
    }

    @Test
    public void writeToFileTest() throws IntersectedTaskException {
        TaskManager taskManager = Managers.getDefault();
        if (!taskManager.getAllTask().isEmpty()) return;
        //Создайте две задачи, а также эпик с двумя подзадачами и  эпик с одной подзадачей.
        Task task = new Task("task1 name", "task1 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2));
        taskManager.addTask(task);

        Task task2 = new Task("task2 name", "task2 description", Status.IN_PROGRESS,
                time.plus(Duration.ofMinutes(25)),
                Duration.ofMinutes(2));
        taskManager.addTask(task2);


        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
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
    }

    @Test
    public void readFromFileTest() throws IntersectedTaskException {
        TaskManager taskManager = Managers.getDefault();
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