package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {
    Task addTask(Task task);

    List<Task> getHistory();
}
