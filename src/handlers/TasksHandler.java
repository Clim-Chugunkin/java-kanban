package handlers;

import exceptions.IntersectedTaskException;
import exceptions.TaskNotFoundException;
import manager.TaskManager;
import task.Task;

import java.util.List;

public class TasksHandler extends TaskHandler {

    public TasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    String getAllTasks(int id) throws TaskNotFoundException {
        List<Task> tasks = manager.getAllTask();
        return gson.toJson(tasks);
    }

    @Override
    String getTaskByID(int id) throws TaskNotFoundException {
        Task task = manager.getTaskByID(id);
        if (task == null) throw new TaskNotFoundException();
        return gson.toJson(task);
    }

    @Override
    void updateTask(String jsonStr, int id) throws IntersectedTaskException {
        Task task = gson.fromJson(jsonStr, Task.class);
        task.setTaskID(id);
        manager.updateTask(task);
    }

    @Override
    void addTask(String jsonStr) throws IntersectedTaskException {
        Task task = gson.fromJson(jsonStr, Task.class);
        task.setTaskID(-1);
        manager.addTask(task);
    }

    @Override
    void eraseTaskByID(int id) throws TaskNotFoundException {
        if (manager.getTaskByID(id) == null) throw new TaskNotFoundException();
        manager.eraseTaskByID(id);
    }
}
