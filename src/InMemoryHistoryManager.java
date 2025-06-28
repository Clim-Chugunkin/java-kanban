import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_DEPTH = 10;
    private final ArrayList<Task> history = new ArrayList<>(HISTORY_DEPTH);


    @Override
    public Task addTask(Task task) {
        if (task == null) return null;
        if (history.size() == HISTORY_DEPTH) {
            //смещаем все элементы влево,
            // первый перезаписываем вторым, третии четвертым итд
            for (int i = 1; i < HISTORY_DEPTH; i++) {
                history.set(i - 1, history.get(i));
            }

            //добавляем в конец новую задачу
            history.set(HISTORY_DEPTH - 1, task);

        } else {
            history.addLast(task);
        }
        return task;
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
