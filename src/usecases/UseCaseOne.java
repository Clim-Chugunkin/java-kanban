package usecases;

import manager.IntersectedTaskException;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class UseCaseOne implements UseCase {

    public String getName() {
        return "UseCaseOne";
    }

    public void go() {
        TaskManager manager = Managers.getDefault();
        //Создайте две задачи, а также эпик с двумя подзадачами и  эпик с одной подзадачей.
        Task task = new Task("task1 name", "task1 description", Status.NEW);
        Epic epic = null;
        Subtask subtask1 = null;
        Subtask subtask2 = null;
        try {
            manager.addTask(task);
            Task task2 = new Task("task2 name", "task2 description", Status.IN_PROGRESS);
            manager.addTask(task2);


            epic = new Epic("epic1 name", "epic1 description", Status.NEW);
            manager.addEpic(epic);
            subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
            subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
            manager.addSubTask(subtask1);
            manager.addSubTask(subtask2);

            Epic epic2 = new Epic("epic2 name", "epic2 description", Status.NEW);
            manager.addEpic(epic2);
            Subtask subtask3 = new Subtask("subtask1 epic2 name", "subtask1 epic2 description", Status.IN_PROGRESS, epic2.getTaskID());
            manager.addSubTask(subtask3);
        } catch (IntersectedTaskException e) {
            System.out.println(e.getMessage());
        }

        //Распечатайте списки эпиков, задач и подзадач через
        System.out.println("Task list : ");
        System.out.println(manager.getAllTask());

        System.out.println("Epic list : ");
        System.out.println(manager.getAllEpic());

        System.out.println("Subtask list : ");
        System.out.println(manager.getAllSubTask());
        //Измените статусы созданных объектов, распечатайте их.
        // Проверьте, что статус задачи и подзадачи сохранился,
        // а статус эпика рассчитался по статусам подзадач
        System.out.println("epic1 status before change " + manager.getEpicByID(epic.getTaskID()).getStatus());
        //change epic1 subtask1 status
        subtask1.setStatus(Status.IN_PROGRESS);
        try {
            manager.updateSubtask(subtask1);
            System.out.println("epic1 subtask1 status : " + manager.getSubtaskByID(subtask1.getTaskID()).getStatus());
            System.out.println("epic1 status after change " + manager.getEpicByID(epic.getTaskID()).getStatus());
            //change all subtask's status of epic1 to DONE
            subtask1.setStatus(Status.DONE);
            subtask2.setStatus(Status.DONE);

            manager.updateSubtask(subtask1);
            manager.updateSubtask(subtask2);
        } catch (IntersectedTaskException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("epic1 status after all subtasks done " + manager.getEpicByID(epic.getTaskID()).getStatus());

        //И, наконец, попробуйте удалить одну из задач и один из эпиков
        System.out.println("deleting task1.... ");
        manager.eraseTaskByID(task.getTaskID());
        System.out.println(manager.getAllTask());

        System.out.println("deleting epic1.... ");
        manager.eraseEpicByID(epic.getTaskID());
        System.out.println(manager.getAllEpic());

    }
}
