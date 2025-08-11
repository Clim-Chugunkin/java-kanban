package task;

import java.util.Objects;

import static task.TaskTypes.*;

public class Task {
    private String name;
    private String description;
    private int taskID = -1;
    private Status status;

    public Task(String[] str) {
        taskID = Integer.parseInt(str[0]);
        name = str[2];
        status = Status.valueOf(str[3]);
        description = str[4];
    }

    public Task(int id) {
        taskID = id;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, int taskID, Status status) {
        this.name = name;
        this.description = description;
        this.taskID = taskID;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskID=" + taskID +
                ", status=" + status +
                '}';
    }

    //Напишите метод сохранения задачи в строку
    public String getString() {
        String[] fields = {Integer.toString(taskID),
                TASK.name(), name, status.name(), description,};
        return String.join(",", fields);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskID == task.taskID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taskID);
    }
}
