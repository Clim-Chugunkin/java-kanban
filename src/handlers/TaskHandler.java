package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.IntersectedTaskException;
import exceptions.TaskNotFoundException;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public abstract class TaskHandler extends BaseHttpHandler {
    protected TaskManager manager;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public String doGet(HttpExchange exchange) throws IntersectedTaskException, TaskNotFoundException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length == 4) {
            if (!path[3].equals("subtasks")) throw new TaskNotFoundException();
            int epicID = Integer.parseInt(path[2]);
            return getAllTasks(epicID);
        }
        if (path.length == 3) {
            return getTaskByID(Integer.parseInt(path[2]));
        }
        return getAllTasks(0);
    }

    @Override
    public String doPost(HttpExchange exchange) throws IntersectedTaskException, IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (path.length >= 3) {
            updateTask(body, Integer.parseInt(path[2]));
            return "задача обновлена";
        }
        addTask(body);
        return "задача добавлена";
    }

    @Override
    public String doDelete(HttpExchange exchange) throws TaskNotFoundException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length < 3) {
            throw new TaskNotFoundException();
        }
        eraseTaskByID(Integer.parseInt(path[2]));
        return "Задача удалена";
    }

    abstract String getAllTasks(int id) throws TaskNotFoundException;

    abstract String getTaskByID(int id) throws TaskNotFoundException;

    abstract void updateTask(String jsonStr, int id) throws IntersectedTaskException;

    abstract void addTask(String jsonStr) throws IntersectedTaskException;

    abstract void eraseTaskByID(int id) throws TaskNotFoundException;

}
