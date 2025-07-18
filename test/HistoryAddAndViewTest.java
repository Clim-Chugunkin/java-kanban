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

    @Test
    public void LinkedListTest() {
        InMemoryHistoryManager hManager = new InMemoryHistoryManager();
        Node<Task>[] nodes = new Node[5];

        //добавляем узлы к списку
        for (int i = 0; i < 5; i++) {
            nodes[i] = hManager.linkLast(new Task("Task " + i, "Task description" + i, NEW));
        }
        //проверяем количество добавленных узлов
        assertEquals(5, hManager.getTasks().size());

        //проверяем содержаться ли добавленные задачи в списке
        for (Node<Task> node : nodes)
            assertTrue(hManager.getTasks().contains(node.getData()));

        //удаляем узлы из списка
        for (int i = 0; i < 5; i++)
            hManager.removeNode(nodes[i]);
        assertEquals(0, hManager.getTasks().size());

    }

    @Test
    public void useCaseOne() {
        //Создайте две задачи,
        Task task = new Task("Task ", "Task description", NEW);
        manager.addTask(task);

        Task task2 = new Task("Task2 ", "Task2 description", NEW);
        manager.addTask(task2);



        //эпик с тремя подзадачами
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        manager.addEpic(epic);

        //создаем и добавляем две подзадачи

        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask3 = new Subtask("subtask3 epic1 name", "subtask3 epic1 description", Status.NEW, epic.getTaskID());

        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        manager.addSubTask(subtask3);

        //и эпик без подзадач.
        Epic epic2 = new Epic("epic2 name", "epic2 description", Status.NEW);
        manager.addEpic(epic2);


        //Запросите созданные задачи несколько раз в разном порядке.
        //После каждого запроса выведите историю и убедитесь, что в ней нет повторов
        int[] correct = {2, 3, 4, 5,1};
        manager.getTaskByID(task.getTaskID());
        manager.getTaskByID(task2.getTaskID());
        manager.getEpicByID(epic.getTaskID());
        manager.getSubtaskByID(subtask1.getTaskID());
        manager.getSubtaskByID(subtask2.getTaskID());
        manager.getTaskByID(task.getTaskID());
        assertArrayEquals(correct, getIDArray());

        //Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        int[] correct2 = {2, 3, 4, 5};
        manager.eraseTaskByID(task.getTaskID());
        assertArrayEquals(correct2, getIDArray());

        //Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи

        int[] correct3 = {2};
        manager.eraseEpicByID(epic.getTaskID());
        assertArrayEquals(correct3, getIDArray());


    }

}
