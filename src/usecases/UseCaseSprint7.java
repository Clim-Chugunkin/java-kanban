package usecases;

import manager.FileBackedTaskManager;
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
        try {
            File f = File.createTempFile("out", ".txt");
            fileName = f.getName();
        } catch (IOException ex) {
            System.out.println("Фаил не может быть создан");
        }

        FileBackedTaskManager manager = new FileBackedTaskManager(fileName);
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

        FileBackedTaskManager manager2 = new FileBackedTaskManager(fileName);
        try {
            manager2.loadFromFile();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            ;
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
