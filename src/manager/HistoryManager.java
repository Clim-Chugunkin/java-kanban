package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {
    Task addTask(Task task);

    void remove(int id);

    List<Task> getHistory();
}
