package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.EndpointNotFoundException;
import exceptions.IntersectedTaskException;
import exceptions.TaskNotFoundException;
import manager.TaskManager;
import task.Task;

import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler {
    TaskManager manager;

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public String doGet(HttpExchange exchange) throws EndpointNotFoundException, IntersectedTaskException, TaskNotFoundException {
        Set<Task> tasks = manager.getPrioritizedTasks();
        return gson.toJson(tasks);
    }
}
