package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.EndpointNotFoundException;
import exceptions.IntersectedTaskException;
import exceptions.TaskNotFoundException;
import manager.TaskManager;
import task.Task;

import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public String doGet(HttpExchange exchange) throws EndpointNotFoundException, IntersectedTaskException, TaskNotFoundException {
        List<Task> tasks = manager.getHistory();
        return gson.toJson(tasks);
    }
}
