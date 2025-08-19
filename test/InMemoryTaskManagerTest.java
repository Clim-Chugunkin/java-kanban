import manager.InMemoryTaskManager;
import manager.IntersectedTaskException;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;


import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void setUP() {
        taskManager = new InMemoryTaskManager();
    }

    //тест на проверку сохранения и сопоставления подзадач в эпике
    @Test
    void testSubtasksInEpic() {
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        int epicID = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());

        int subtaskID = 0;
        try {
            subtaskID = taskManager.addSubTask(subtask1);
            taskManager.addSubTask(subtask2);
        } catch (IntersectedTaskException e) {
            System.out.println(e.getMessage());
        }


        ArrayList<Subtask> subtasks = taskManager.getAllEpicSubtask(epicID);
        assertTrue(subtasks.contains(taskManager.getSubtaskByID(subtaskID)));

    }

}