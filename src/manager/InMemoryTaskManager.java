package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager history = Managers.getDefaultHistory();
    private int countID = 1;


    @Override
    //методы для  создания задачи, епика, подзадачи  (пункт 2.d)
    public int addTask(Task task) {
        if (task.getTaskID() < 0) {
            task.setTaskID(countID++);
        } else {
            if (countID <= task.getTaskID()) countID = task.getTaskID() + 1;
        }
        tasks.put(task.getTaskID(), task);
        return task.getTaskID();
    }

    @Override
    public int addEpic(Epic epic) {
        if (epic.getTaskID() < 0) {
            epic.setTaskID(countID++);
        } else {
            if (countID <= epic.getTaskID()) countID = epic.getTaskID() + 1;
        }
        epics.put(epic.getTaskID(), epic);
        return epic.getTaskID();
    }

    @Override
    public int addSubTask(Subtask subtask) {
        if (subtask.getTaskID() < 0) {
            subtask.setTaskID(countID++);
        } else {
            if (countID <= subtask.getTaskID()) countID = subtask.getTaskID() + 1;
        }
        subtasks.put(subtask.getTaskID(), subtask);

        //обновляем статус епика
        int epicID = subtask.getEpicID();
        updateEpicStatus(epicID);
        return subtask.getTaskID();
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
        //удаляем все задачи из истории
        for (Task task : tasks.values())
            history.remove(task.getTaskID());
        tasks.clear();
    }

    @Override
    public void clearEpics() {

        for (Epic epic : epics.values())
            history.remove(epic.getTaskID());
        epics.clear();
    }

    @Override
    public void clearSubtask() {
        for (Subtask subtask : subtasks.values())
            history.remove(subtask.getTaskID());
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
        updateEpicStatus(epicID);
    }

    //методы для  удаления по идентификатору  (пункт 2.e)
    @Override
    public void eraseTaskByID(int id) {

        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void eraseEpicByID(int id) {
        //перед удалением эпика
        //удаляем его подзадачи из subtasks
        //и истории

        //для перебора subtasks и удаление нужных элементов
        //используем итереатор

        Iterator<Subtask> iterator = subtasks.values().iterator();
        Subtask subtask;
        while (iterator.hasNext()) {
            subtask = iterator.next();
            if (subtask.getTaskID() == id) iterator.remove();
            history.remove(subtask.getTaskID());
        }

        epics.remove(id);
        history.remove(id);
    }

    @Override
    public void eraseSubtaskByID(int id) {
        int epicID = subtasks.get(id).getEpicID();
        subtasks.remove(id);
        updateEpicStatus(epicID);
        history.remove(id);
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

    private void updateEpicStatus(int epicID) {
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
        Status status = Status.IN_PROGRESS;

        if (((statusNew == count) || count == 0)) status = Status.NEW;
        else if ((statusDone == count)) status = Status.DONE;

        epics.get(epicID).setStatus(status);
    }

}
