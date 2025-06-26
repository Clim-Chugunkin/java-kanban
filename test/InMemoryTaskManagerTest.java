import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void setUP(){
        taskManager = Managers.getDefault();
    }
   //тест на добавление новой задачи
    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    //тест на проверку сохранения и сопоставления подзадач в эпике
    @Test
    void testSubtasksInEpic(){
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        int epicID = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());

        int subtaskID = taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);


        ArrayList<Subtask> subtasks  = taskManager.getAllEpicSubtask(epicID);
        assertTrue(subtasks.contains(taskManager.getSubtaskByID(subtaskID)));

    }
    //тест на проверку неизменности данных задачи
    @Test
    void checkTaskImmutability(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.addTask(task);
        Task savedTask = taskManager.getTaskByID(taskId);

        assertEquals("Test addNewTask", savedTask.getName());
        assertEquals("Test addNewTask description",savedTask.getDescription());
        assertEquals(NEW,savedTask.getStatus());
    }

    //
    @Test
    void historyCheck(){
        //создаем и добавляем новую задачу
        Task task = new Task("Task ", "Task description", NEW);
        taskManager.addTask(task);

        //создаем и добавляем эпик
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        taskManager.addEpic(epic);

        //создаем и добавляем две подзадачи

        Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
        Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);


        //моделируем историю просмотров
        //записываем 5 элементов в истроию
        int []correct = {1,1,2,3,4};
        taskManager.getTaskByID(task.getTaskID());
        taskManager.getTaskByID(task.getTaskID());
        taskManager.getEpicByID(epic.getTaskID());
        taskManager.getSubtaskByID(subtask1.getTaskID());
        taskManager.getSubtaskByID(subtask2.getTaskID());
        assertArrayEquals(correct, getIDArray() );


        //записываем еще 5 элементов в истроию
        //граничное значение
        int []correct2 = {1,1,2,3,4,1,1,2,3,4};
        taskManager.getTaskByID(task.getTaskID());
        taskManager.getTaskByID(task.getTaskID());
        taskManager.getEpicByID(epic.getTaskID());
        taskManager.getSubtaskByID(subtask1.getTaskID());
        taskManager.getSubtaskByID(subtask2.getTaskID());
        assertArrayEquals(correct2, getIDArray() );

       //записываем 1 значение которе должно записаться в конец
        // и история сдвинеться
        int []correct3 = {1,2,3,4,1,1,2,3,4,1};
        taskManager.getTaskByID(task.getTaskID());
        assertArrayEquals(correct3, getIDArray() );

        //записываем 2 значение которе должно записаться в конец
        // и история сдвинеться
        int []correct4 = {3,4,1,1,2,3,4,1,2,3};
        taskManager.getEpicByID(epic.getTaskID());
        taskManager.getSubtaskByID(subtask1.getTaskID());
        assertArrayEquals(correct4, getIDArray() );
    }

    private int[] getIDArray(){
        int size = taskManager.getHistory().size();
        int [] array = new int[size];
        for (int i = 0; i<size; i++){
            array[i] = taskManager.getHistory().get(i).getTaskID();
        }
        return array;
    }


}