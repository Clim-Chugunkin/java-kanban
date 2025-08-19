package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static task.TaskTypes.*;

public class Task implements Comparable<Task> {
    private String name;
    private String description;
    private int taskID = -1;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String[] str) {
        taskID = Integer.parseInt(str[0]);
        name = str[2];
        status = Status.valueOf(str[3]);
        description = str[4];

        startTime = (!str[5].equals("0")) ? LocalDateTime.parse(str[5]) : null;
        duration = (!str[6].equals("0")) ? Duration.parse(str[6]) : null;
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

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
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

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskID=" + taskID +
                ", status=" + status +
                ", startTime =" + startTime +
                ", duration = " + duration +
                '}';
    }

    //Напишите метод сохранения задачи в строку
    public String getString() {
        String[] fields = {Integer.toString(taskID),
                TASK.name(), name, status.name(), description, (startTime != null) ? startTime.toString() : "0",
                (duration != null) ? duration.toString() : "0"};
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime time) {
        startTime = time;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(Task o) {
        //если время не задано устанавливаем самый низкии приоритет
        if ((this.startTime == null) || (this.duration == null)) return 2;
        if ((o.startTime == null) || (o.duration == null)) return 2;
        if (o.startTime.isAfter(this.startTime)) return -1;
        if (o.startTime.equals(this.startTime)) return 0;
        return 1;
    }
}
