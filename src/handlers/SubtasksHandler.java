package handlers;

import exceptions.IntersectedTaskException;
import exceptions.TaskNotFoundException;
import manager.TaskManager;
import task.Subtask;

import java.util.List;

public class SubtasksHandler extends TasksHandler {

    public SubtasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    String getAllTasks(int id) throws TaskNotFoundException {
        List<Subtask> subtasks = manager.getAllSubTask();
        return gson.toJson(subtasks);
    }

    @Override
    String getTaskByID(int id) throws TaskNotFoundException {
        Subtask subtasks = manager.getSubtaskByID(id);
        if (subtasks == null) throw new TaskNotFoundException();
        return gson.toJson(subtasks);
    }

    @Override
    void updateTask(String jsonStr, int id) throws IntersectedTaskException {
        Subtask subtask = gson.fromJson(jsonStr, Subtask.class);
        subtask.setTaskID(id);
        manager.updateSubtask(subtask);
    }

    @Override
    void addTask(String jsonStr) throws IntersectedTaskException {
        Subtask subtask = gson.fromJson(jsonStr, Subtask.class);
        subtask.setTaskID(-1);
        manager.addSubTask(subtask);
    }

    @Override
    void eraseTaskByID(int id) throws TaskNotFoundException {
        if (manager.getSubtaskByID(id) == null) throw new TaskNotFoundException();
        manager.eraseSubtaskByID(id);
    }
}
