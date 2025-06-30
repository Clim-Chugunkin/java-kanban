import manager.Managers;
import manager.TaskManager;
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
        manager = Managers.getDefault();

    }

    @Test
    void historyCheck() {
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


        //моделируем историю просмотров
        //записываем 5 элементов в истроию
        int[] correct = {1, 1, 2, 3, 4};
        manager.getTaskByID(task.getTaskID());
        manager.getTaskByID(task.getTaskID());
        manager.getEpicByID(epic.getTaskID());
        manager.getSubtaskByID(subtask1.getTaskID());
        manager.getSubtaskByID(subtask2.getTaskID());
        assertArrayEquals(correct, getIDArray());


        //записываем еще 5 элементов в истроию
        //граничное значение
        int[] correct2 = {1, 1, 2, 3, 4, 1, 1, 2, 3, 4};
        manager.getTaskByID(task.getTaskID());
        manager.getTaskByID(task.getTaskID());
        manager.getEpicByID(epic.getTaskID());
        manager.getSubtaskByID(subtask1.getTaskID());
        manager.getSubtaskByID(subtask2.getTaskID());
        assertArrayEquals(correct2, getIDArray());

        //записываем 1 значение которе должно записаться в конец
        // и история сдвинеться
        int[] correct3 = {1, 2, 3, 4, 1, 1, 2, 3, 4, 1};
        manager.getTaskByID(task.getTaskID());
        assertArrayEquals(correct3, getIDArray());

        //записываем 2 значение которе должно записаться в конец
        // и история сдвинеться
        int[] correct4 = {3, 4, 1, 1, 2, 3, 4, 1, 2, 3};
        manager.getEpicByID(epic.getTaskID());
        manager.getSubtaskByID(subtask1.getTaskID());
        assertArrayEquals(correct4, getIDArray());
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
