import exceptions.IntersectedTaskException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import static task.Status.NEW;

public class Sprint6Test {


    @Test
    public void sprint6Test() {
        TaskManager manager = null;
        Task task = null;
        Task task2 = null;
        Epic epic = null;
        Subtask subtask1 = null;
        Subtask subtask2 = null;
        try {
            manager = Managers.getInMemoryTaskManager();
            //Создайте две задачи,
            task = new Task("Task ", "Task description", NEW);
            manager.addTask(task);
            task2 = new Task("Task2 ", "Task2 description", NEW);
            manager.addTask(task2);
            //эпик с тремя подзадачами
            epic = new Epic("epic1 name", "epic1 description", Status.NEW);
            manager.addEpic(epic);
            //создаем и добавляем две подзадачи
            subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
            subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
            Subtask subtask3 = new Subtask("subtask3 epic1 name", "subtask3 epic1 description", Status.NEW, epic.getTaskID());
            manager.addSubTask(subtask1);
            manager.addSubTask(subtask2);
            manager.addSubTask(subtask3);
        } catch (IntersectedTaskException e) {
            System.out.println(e.getMessage());
        }
        //и эпик без подзадач.
        Epic epic2 = new Epic("epic2 name", "epic2 description", Status.NEW);
        manager.addEpic(epic2);
        //Запросите созданные задачи несколько раз в разном порядке.
        //После каждого запроса выведите историю и убедитесь, что в ней нет повторов
        System.out.println("запрашиваем задачи в разном порядке и выводим историю");
        manager.getTaskByID(task.getTaskID());
        manager.getTaskByID(task2.getTaskID());
        manager.getEpicByID(epic.getTaskID());
        manager.getSubtaskByID(subtask1.getTaskID());
        manager.getSubtaskByID(subtask2.getTaskID());
        manager.getTaskByID(task.getTaskID());
        for (Task job : manager.getHistory())
            System.out.println(job);

        //Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        System.out.println("Удалите задачу, которая есть в истории");
        manager.eraseTaskByID(task.getTaskID());
        for (Task job : manager.getHistory())
            System.out.println(job);
        //Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи
        System.out.println("Удалием эпик с тремя подзадачами");
        manager.eraseEpicByID(epic.getTaskID());
        System.out.println(manager.getHistory());
    }
}
