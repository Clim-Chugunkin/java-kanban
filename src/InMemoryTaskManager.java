import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {


    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    HistoryManager history = Managers.getDefaultHistory();
    private int countID = 1;


    @Override
    //методы для  создания задачи, епика, подзадачи  (пункт 2.d)
    public int addTask(Task task) {
        task.setTaskID(countID);
        tasks.put(task.getTaskID(), task);
        return countID++;
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setTaskID(countID);
        epic.setStatus(getEpicStatus(epic.getTaskID()));
        epics.put(epic.getTaskID(), epic);
        return countID++;
    }

    @Override
    public int addSubTask(Subtask subtask) {
        subtask.setTaskID(countID);
        subtasks.put(subtask.getTaskID(), subtask);
        //обновляем статус епика
        int epicID = subtask.getEpicID();
        epics.get(epicID).setStatus(getEpicStatus(epicID));
        return countID++;
    }

    //методы для  получения всех задач   (пункт 2.a)
    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubTask() {
        return new ArrayList<>(subtasks.values());
    }

    //методы для  удаления всех задач   (пункт 2.b)
    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
    }

    @Override
    public void clearSubtask() {

        subtasks.clear();
        //updating all epics
        //если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
    }

    //методы для  получения по идентификатору   (пункт 2.c)
    @Override
    public Task getTaskByID(int id) {
        return history.addTask(tasks.get(id));
    }

    @Override
    public Epic getEpicByID(int id) {
        return (Epic) history.addTask(epics.get(id));

    }

    @Override
    public Subtask getSubtaskByID(int id) {
        return (Subtask) history.addTask(subtasks.get(id));
    }

    //методы для  обновления  (пункт 2.e)
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskID(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskID(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskID(), subtask);
        //обновляем статус епика
        int epicID = subtask.getEpicID();
        epics.get(epicID).setStatus(getEpicStatus(epicID));
    }

    //методы для  удаления по идентификатору  (пункт 2.e)
    @Override
    public void eraseTaskByID(int id) {
        tasks.remove(id);
    }

    @Override
    public void eraseEpicByID(int id) {
        epics.remove(id);
    }

    @Override
    public void eraseSubtaskByID(int id) {
        int epicID = subtasks.get(id).getEpicID();
        subtasks.remove(id);
        epics.get(epicID).setStatus(getEpicStatus(epicID));
    }

    //получение списка задач епика 3.a
    @Override
    public ArrayList<Subtask> getAllEpicSubtask(int epicID) {
        ArrayList<Subtask> epicTasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicID() == epicID) epicTasks.add(subtask);
        }
        return epicTasks;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private Status getEpicStatus(int epicID) {
        int statusNew = 0;
        int statusDone = 0;
        int count = 0;
        if (epics.containsKey(epicID)) {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicID() == epicID) {
                    switch (subtask.getStatus()) {
                        case NEW -> statusNew++;
                        case DONE -> statusDone++;
                    }
                    count++;
                }
            }
        }
        // if all subtask has status new or epic is empty => epic status is new
        if ((statusNew == count) || count == 0) return Status.NEW;
        if (statusDone == count) return Status.DONE;
        return Status.IN_PROGRESS;
    }

}
