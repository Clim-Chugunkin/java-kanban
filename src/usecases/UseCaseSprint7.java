package usecases;

import manager.FileBackedTaskManager;
import manager.IntersectedTaskException;
import manager.ManagerSaveException;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UseCaseSprint7 implements UseCase {
    @Override
    public String getName() {
        return "UseCaseSprint7";
    }

    @Override
    public void go() {
        String fileName = "";
        File file = null;
        FileBackedTaskManager manager = null;
        try {
            file = File.createTempFile("out", ".txt");
            //fileName = f.getName();
            manager = FileBackedTaskManager.loadFromFile(file);
        } catch (IOException ex) {
            System.out.println("Фаил не может быть создан");
        } catch (ManagerSaveException | IntersectedTaskException e) {
            System.out.println(e.getMessage());
        }


        FileBackedTaskManager manager2 = null;
        try {
            //Заведите несколько разных задач, эпиков и подзадач.
            Task task = new Task("task1 name", "task1 description", Status.NEW);
            manager.addTask(task);
            Task task2 = new Task("task2 name", "task2 description", Status.IN_PROGRESS);
            manager.addTask(task2);


            Epic epic = new Epic("epic1 name", "epic1 description", Status.NEW);
            manager.addEpic(epic);
            Subtask subtask1 = new Subtask("subtask1 epic1 name", "subtask1 epic1 description", Status.NEW, epic.getTaskID());
            Subtask subtask2 = new Subtask("subtask2 epic1 name", "subtask2 epic1 description", Status.NEW, epic.getTaskID());
            manager.addSubTask(subtask1);
            manager.addSubTask(subtask2);

            Epic epic2 = new Epic("epic2 name", "epic2 description", Status.NEW);
            manager.addEpic(epic2);
            Subtask subtask3 = new Subtask("subtask1 epic2 name", "subtask1 epic2 description", Status.IN_PROGRESS, epic2.getTaskID());
            manager.addSubTask(subtask3);

            manager2 = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException  | IntersectedTaskException ex) {
            System.out.println(ex);
        }

        List<Task> oldTasks = manager.getAllTask();
        List<Task> newTasks = manager2.getAllTask();
        if (oldTasks.size() != newTasks.size()) System.out.println("задачи отличаются");
        else {
            for (int i = 0; i < oldTasks.size(); i++) {
                if (!oldTasks.get(i).equals(newTasks.get(i))) {
                    System.out.println("задачи отличаются");
                    return;
                }
            }
            System.out.println("задачи одинаковы");
        }

    }
}
