package task;

import java.time.Duration;
import java.time.LocalDateTime;

import static task.TaskTypes.*;

public class Subtask extends Task {

    private int epicID;

    public Subtask(String name, String description, Status status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public Subtask(String name, String description, Status status, LocalDateTime startTime, Duration duration, int epicID) {
        super(name, description, status, startTime, duration);
        this.epicID = epicID;
    }

    public Subtask(String[] str) {
        super(str);
        epicID = Integer.parseInt(str[7]);
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String getString() {
        return super.getString().replace(TASK.name(), SUBTASK.name()) + "," + Integer.toString(epicID);
    }

}
