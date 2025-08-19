import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

public class HistoryAddAndViewTest {
    TaskManager manager;


    @BeforeEach
    void setUP() {
        manager = Managers.getInMemoryTaskManager();

    }

    @Test
    void historyCheck() throws IntersectedTaskException {
        //создаем и добавляем новую задачу
        Task task = new Task("Task ", "Task description", NEW);
        manager.addTask(task);

        //создаем и добавляем эпик
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        manager.addEpic(epic);

        //создаем и добавляем две подзадачи

        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        //добавляем в историю задачи по очереди
        int[] correct = {1, 2, 3, 4};
        manager.getTaskByID(task.getTaskID());
        manager.getTaskByID(task.getTaskID());
        manager.getEpicByID(epic.getTaskID());
        manager.getSubtaskByID(subtask1.getTaskID());
        manager.getSubtaskByID(subtask2.getTaskID());
        assertArrayEquals(correct, getIDArray());


        int[] correct2 = {2, 3, 4, 1};
        manager.getTaskByID(task.getTaskID());
        assertArrayEquals(correct2, getIDArray());

        int[] correct3 = {2, 4, 1, 3};
        manager.getSubtaskByID(subtask1.getTaskID());
        assertArrayEquals(correct3, getIDArray());


        //удалим задачу из taskManager
        int[] correct4 = {2, 4, 3};
        manager.eraseTaskByID(task.getTaskID());

        assertArrayEquals(correct4, getIDArray());


        //удалим epic из taskManager
        manager.addTask(task);
        int[] correct5 = {task.getTaskID()};
        manager.getTaskByID(task.getTaskID());
        manager.eraseEpicByID(epic.getTaskID());
        assertArrayEquals(correct5, getIDArray());

    }

    private int[] getIDArray() {
        int size = manager.getHistory().size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = manager.getHistory().get(i).getTaskID();
        }
        return array;
    }

}
