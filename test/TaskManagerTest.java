import exceptions.IntersectedTaskException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;


    @Test
    public void addTaskTest() throws IntersectedTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskID = taskManager.addTask(task);
        final Task savedTask = taskManager.getTaskByID(taskID);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getAllTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addEpicTest() {
        Epic epic = new Epic("Epic1", "Epic1 description", NEW);
        final int epicID = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpicByID(epicID);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void addSubtaskTest() throws IntersectedTaskException {
        Epic epic = new Epic("Epic1", "Epic1 description", NEW);
        final int epicID = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Subtask1 description", NEW, epicID);
        final int subtaskID = taskManager.addSubTask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskByID(subtaskID);
        assertNotNull(savedSubtask, "подзадача не найденf.");
        assertEquals(subtask, savedSubtask, "подзадачb не совпадают.");
        final List<Subtask> subtasks = taskManager.getAllSubTask();
        assertNotNull(subtasks, "подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "подзадачи не совпадают.");
    }

    @Test
    public void getAllTaskTest() throws IntersectedTaskException {
        assertEquals(0, taskManager.getAllTask().size(), "Неверное количество подзадач.");
        Task task1 = new Task("task1", "task1 description", NEW);
        Task task2 = new Task("task2", "task2 description", NEW);
        Task task3 = new Task("task3", "task3 description", NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        assertEquals(3, taskManager.getAllTask().size(), "Неверное количество подзадач.");
        assertEquals(task1, taskManager.getAllTask().get(0), "подзадачи не совпадают.");
    }

    @Test
    public void getAllEpicTest() throws IntersectedTaskException {
        assertEquals(0, taskManager.getAllEpic().size(), "Неверное количество эпиков.");
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        Epic epic2 = new Epic("epic2", "epic2 description", NEW);
        Epic epic3 = new Epic("epic3", "epic3 description", NEW);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        assertEquals(3, taskManager.getAllEpic().size(), "Неверное количество эпиков.");
        assertEquals(epic1, taskManager.getAllEpic().get(0), "эпики не совпадают.");
    }

    @Test
    public void getAllSubtaskTest() throws IntersectedTaskException {
        assertEquals(0, taskManager.getAllEpic().size(), "Неверное количество эпиков.");
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        int epicID = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("epic1", "epic1 description", NEW, epicID);
        Subtask subtask2 = new Subtask("epic2", "epic2 description", NEW, epicID);
        Subtask subtask3 = new Subtask("epic3", "epic3 description", NEW, epicID);
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);
        assertEquals(3, taskManager.getAllSubTask().size(), "Неверное количество эпиков.");
        assertEquals(subtask1, taskManager.getAllSubTask().get(0), "эпики не совпадают.");
    }

    @Test
    public void getTaskByIDTest() throws IntersectedTaskException {
        assertNull(taskManager.getTaskByID(1));
        Task task1 = new Task("task1", "task1 description", NEW);
        int taskID = taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskByID(taskID), "эпики не совпадают.");
        assertNull(taskManager.getTaskByID(taskID + 1));
    }

    @Test
    public void getEpicByIDTest() throws IntersectedTaskException {
        assertNull(taskManager.getEpicByID(1));
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        int epicID = taskManager.addEpic(epic1);
        assertEquals(epic1, taskManager.getEpicByID(epicID), "эпики не совпадают.");
        assertNull(taskManager.getEpicByID(epicID + 1));
    }

    @Test
    public void getSubtaskByIDTest() throws IntersectedTaskException {
        assertNull(taskManager.getSubtaskByID(1));
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        int epicID = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("epic1", "epic1 description", NEW, epicID);
        int subtaskID = taskManager.addSubTask(subtask1);
        assertEquals(subtask1, taskManager.getSubtaskByID(subtaskID), "эпики не совпадают.");
        assertNull(taskManager.getSubtaskByID(subtaskID + 1));
    }

    @Test
    public void updateTaskTest() throws IntersectedTaskException {
        Task task1 = new Task("task1", "task1 description", NEW);
        Task task2 = new Task("task2", "task2 description", NEW);
        int task1ID = taskManager.addTask(task1);
        task2.setTaskID(task1ID);
        taskManager.updateTask(task2);
        assertEquals(task2, taskManager.getTaskByID(task1ID), "задачи не совпадают.");
    }

    @Test
    public void updateEpicTest() throws IntersectedTaskException {
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        Epic epic2 = new Epic("epic2", "epic2 description", NEW);
        int epic1ID = taskManager.addEpic(epic1);
        epic2.setTaskID(epic1ID);
        taskManager.updateEpic(epic2);
        assertEquals(epic2, taskManager.getEpicByID(epic1ID), "эпики не совпадают.");
    }

    @Test
    public void updateSubtaskTest() throws IntersectedTaskException {
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        int epicID = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("epic1", "epic1 description", NEW, epicID);
        Subtask subtask2 = new Subtask("epic1", "epic1 description", NEW, epicID);
        int subtaskID = taskManager.addSubTask(subtask1);
        subtask2.setTaskID(subtaskID);
        taskManager.updateSubtask(subtask2);
        assertEquals(subtask2, taskManager.getSubtaskByID(subtaskID), "эпики не совпадают.");
    }

    @Test
    public void eraseTaskByIDTest() throws IntersectedTaskException {
        Task task1 = new Task("task1", "task1 description", NEW);
        int taskID = taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskByID(taskID));
        taskManager.eraseTaskByID(taskID);
        assertNull(taskManager.getTaskByID(taskID));
    }

    @Test
    public void eraseEpicByIDTest() throws IntersectedTaskException {
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        int epicID = taskManager.addEpic(epic1);
        assertEquals(epic1, taskManager.getEpicByID(epicID));
        taskManager.eraseTaskByID(epicID);
        assertNull(taskManager.getTaskByID(epicID));
    }

    @Test
    public void eraseSubtaskByID() throws IntersectedTaskException {
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        int epicID = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("epic1", "epic1 description", NEW, epicID);
        int subtaskID = taskManager.addSubTask(subtask1);
        assertEquals(subtask1, taskManager.getSubtaskByID(subtaskID));
        taskManager.eraseSubtaskByID(subtaskID);
        assertNull(taskManager.getSubtaskByID(subtaskID));
    }

    @Test
    public void getAllEpicSubtaskTest() throws IntersectedTaskException {
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        Epic epic2 = new Epic("epic2", "epic2 description", NEW);

        int epic1ID = taskManager.addEpic(epic1);
        int epic2ID = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", NEW, epic1ID);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", NEW, epic1ID);
        Subtask subtask3 = new Subtask("subtask3", "subtask3 description", NEW, epic2ID);
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);
        assertTrue(taskManager.getAllEpicSubtask(epic1ID).contains(subtask1));
        assertTrue(taskManager.getAllEpicSubtask(epic1ID).contains(subtask2));
        assertFalse(taskManager.getAllEpicSubtask(epic1ID).contains(subtask3));
    }

    @Test
    public void getHistoryTest() throws IntersectedTaskException {
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
        //добавляем в историю задачи по очереди
        int[] correct = {1, 2, 3, 4};
        taskManager.getTaskByID(task.getTaskID());
        taskManager.getTaskByID(task.getTaskID());
        taskManager.getEpicByID(epic.getTaskID());
        taskManager.getSubtaskByID(subtask1.getTaskID());
        taskManager.getSubtaskByID(subtask2.getTaskID());
        assertArrayEquals(correct, getIDArray());
        int[] correct2 = {2, 3, 4, 1};
        taskManager.getTaskByID(task.getTaskID());
        assertArrayEquals(correct2, getIDArray());
        int[] correct3 = {2, 4, 1, 3};
        taskManager.getSubtaskByID(subtask1.getTaskID());
        assertArrayEquals(correct3, getIDArray());
        //удалим задачу из taskManager
        int[] correct4 = {2, 4, 3};
        taskManager.eraseTaskByID(task.getTaskID());
        assertArrayEquals(correct4, getIDArray());
        //удалим epic из taskManager
        taskManager.addTask(task);
        int[] correct5 = {task.getTaskID()};
        taskManager.getTaskByID(task.getTaskID());
        taskManager.eraseEpicByID(epic.getTaskID());
        assertArrayEquals(correct5, getIDArray());
    }

    private int[] getIDArray() {
        int size = taskManager.getHistory().size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = taskManager.getHistory().get(i).getTaskID();
        }
        return array;
    }

    @Test
    public void getPrioritizedTasksTest() throws IntersectedTaskException {
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
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);
        taskManager.addSubTask(subtask4);
        assertEquals(subtask4, taskManager.getPrioritizedTasks().toArray()[0]);
        assertEquals(subtask1, taskManager.getPrioritizedTasks().toArray()[1]);
        assertEquals(subtask2, taskManager.getPrioritizedTasks().toArray()[2]);
        assertEquals(subtask3, taskManager.getPrioritizedTasks().toArray()[3]);
    }

    //тест на проверку неизменности данных задачи
    @Test
    void checkTaskImmutability() throws IntersectedTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.addTask(task);
        Task savedTask = taskManager.getTaskByID(taskId);
        assertEquals("Test addNewTask", savedTask.getName());
        assertEquals("Test addNewTask description", savedTask.getDescription());
        assertEquals(NEW, savedTask.getStatus());
    }

    @Test
    void clearTasksTest() throws IntersectedTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        //добавляем две задачи
        taskManager.addTask(task);
        Task task2 = new Task("task2", "task2 description", NEW);
        taskManager.addTask(task2);
        //удаляем все задачи и проверяем
        taskManager.clearTasks();
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void clearEpicTest() throws IntersectedTaskException {
        Epic epic = new Epic("epic", "epic description", NEW);
        //добавляем две задачи
        taskManager.addEpic(epic);
        Epic epic2 = new Epic("epic2", "epic2 description", NEW);
        taskManager.addEpic(epic2);
        //удаляем все задачи и проверяем
        taskManager.clearEpics();
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    public void clearAllSubtaskTest() throws IntersectedTaskException {
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);
        Epic epic2 = new Epic("epic2", "epic2 description", NEW);
        int epic1ID = taskManager.addEpic(epic1);
        int epic2ID = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", NEW, epic1ID);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", NEW, epic1ID);
        Subtask subtask3 = new Subtask("subtask3", "subtask3 description", NEW, epic2ID);
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);
        taskManager.clearSubtask();
        assertEquals(0, taskManager.getAllSubTask().size());
        assertEquals(0, taskManager.getAllEpicSubtask(epic1ID).size());
        assertEquals(0, taskManager.getAllEpicSubtask(epic2ID).size());
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

    //тест на проверку статуса эпика
    @Test
    public void epicTest() throws IntersectedTaskException {
        //Для расчёта статуса Epic.
        TaskManager taskManager = Managers.getInMemoryTaskManager();
        Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
        int epicID = taskManager.addEpic(epic);
        //a. Пустой список подзадач
        assertEquals(Status.NEW, epic.getStatus());
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
        //Для подзадач нужно дополнительно проверить наличие эпика
        for (Subtask subtask : taskManager.getAllSubTask())
            assertNotNull(taskManager.getEpicByID(subtask.getEpicID()));

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
