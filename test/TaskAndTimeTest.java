import manager.InMemoryTaskManager;
import manager.IntersectedTaskException;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskAndTimeTest {
    @Test
    public void EpicStartTimeAndDurationTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime time = LocalDateTime.of(2025, 8, 1, 10, 0, 0);

        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        int epicID = taskManager.addEpic(epic);
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


        try {
            taskManager.addSubTask(subtask1);
            taskManager.addSubTask(subtask2);
            taskManager.addSubTask(subtask3);
            taskManager.addSubTask(subtask4);

        } catch (IntersectedTaskException e) {
            System.out.println(e.getMessage());
        }


        taskManager.getPrioritizedTasks()
                .forEach(System.out::println);
    }
}
