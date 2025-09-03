package handlers;

import exceptions.IntersectedTaskException;
import exceptions.TaskNotFoundException;
import manager.TaskManager;
import task.Epic;

import java.util.List;

public class EpicsHandler extends TaskHandler {


    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    String getAllTasks(int id) throws TaskNotFoundException {
        if (id != 0) {
            if (manager.getEpicByID(id) == null) {
                throw new TaskNotFoundException();
            }
            return gson.toJson(manager.getAllEpicSubtask(id));
        }

        List<Epic> epics = manager.getAllEpic();
        return gson.toJson(epics);
    }

    @Override
    String getTaskByID(int id) throws TaskNotFoundException {
        Epic epic = manager.getEpicByID(id);
        if (epic == null) throw new TaskNotFoundException();
        return gson.toJson(epic);
    }

    @Override
    void updateTask(String jsonStr, int id) throws IntersectedTaskException {
        Epic epic = gson.fromJson(jsonStr, Epic.class);
        epic.setTaskID(id);
        manager.updateEpic(epic);
    }

    @Override
    void addTask(String jsonStr) throws IntersectedTaskException {
        Epic epic = gson.fromJson(jsonStr, Epic.class);
        epic.setTaskID(-1);
        manager.addEpic(epic);
    }

    @Override
    void eraseTaskByID(int id) throws TaskNotFoundException {
        if (manager.getEpicByID(id) == null) throw new TaskNotFoundException();
        manager.eraseEpicByID(id);
    }
}
