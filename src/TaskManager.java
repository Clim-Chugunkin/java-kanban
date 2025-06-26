import task.*;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {

    //методы для  создания задачи, епика, подзадачи  (пункт 2.d)
    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubTask(Subtask subtask);

    //методы для  получения всех задач   (пункт 2.a)
    ArrayList<Task> getAllTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubTask();

    //методы для  удаления всех задач   (пункт 2.b)
    void clearTasks();

    void clearEpics();

    void clearSubtask();

    //методы для  получения по идентификатору   (пункт 2.c)
    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    //методы для  обновления  (пункт 2.e)
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    //методы для  удаления по идентификатору  (пункт 2.e)
    void eraseTaskByID(int id);

    void eraseEpicByID(int id);

    void eraseSubtaskByID(int id);

    //получение списка задач епика 3.a
    ArrayList<Subtask> getAllEpicSubtask(int epicID);

    List<Task> getHistory();
}
