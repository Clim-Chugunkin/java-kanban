import task.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int countID;

    public TaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        countID = 1;
    }
//методы для  создания задачи, епика, подзадачи  (пункт 2.d)
    public void addTask(Task task){
        task.setTaskID(countID++);
        tasks.put(task.getTaskID(),task);
    }

    public void addEpic(Epic epic){
        epic.setTaskID(countID++);
        epics.put(epic.getTaskID(),epic);
    }

    public void addSubTask(Subtask subtask){
        subtask.setTaskID(countID++);
        subtasks.put(subtask.getTaskID(),subtask);
    }
//методы для  получения всех задач   (пункт 2.a)
    public ArrayList<Task> getAllTask(){
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpic(){
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubTask(){
        return new ArrayList<>(subtasks.values());
    }

}
