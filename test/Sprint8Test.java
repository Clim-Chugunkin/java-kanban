import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class Sprint8Test {
    @Test
    public void EpicTest() throws IntersectedTaskException {
        //Для расчёта статуса Epic.
        TaskManager taskManager = Managers.getInMemoryTaskManager();
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        int epicID = taskManager.addEpic(epic);
        //a. Все подзадачи со статусом NEW.
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());

        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask1);
        assertEquals(Status.NEW, epic.getStatus());


        //Подзадачи со статусами NEW и DONE.
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        //Все подзадачи со статусом DONE
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        assertEquals(Status.DONE, epic.getStatus());

        //Все подзадачи со статусом IN_PROGRESS
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    //тест на проверку пересечения интервалов
    @Test
    public void interceptionTestAndPriorityTest() throws IntersectedTaskException {
        TaskManager taskManager = Managers.getInMemoryTaskManager();
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);
        //Создайте две задачи, а также эпик с двумя подзадачами и  эпик с одной подзадачей.
        Task task = new Task("task1 name", "task1 description", Status.NEW,
                time.plus(Duration.ofMinutes(20)),
                Duration.ofMinutes(2));

        taskManager.addTask(task);


        Task task2 = new Task("task2 name", "task2 description", Status.IN_PROGRESS,
                time.plus(Duration.ofMinutes(25)),
                Duration.ofMinutes(2));
        assertDoesNotThrow(() -> {
            taskManager.addTask(task2);
        }, "Exception");

        Task task3 = new Task("task3 name", "task3 description", Status.IN_PROGRESS,
                time.plus(Duration.ofMinutes(25)),
                Duration.ofMinutes(2));

        assertThrows(IntersectedTaskException.class, () -> {
            taskManager.addTask(task3);
        }, "Вызвало исключение пересечения");

        //проверка на приоритет задачи
        Task highPriority = taskManager.getPrioritizedTasks().stream().findFirst().orElse(null);
        Assertions.assertEquals(highPriority, task);

    }


}
