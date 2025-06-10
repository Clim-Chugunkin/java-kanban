package task;

public class Subtask extends Task{

    private int epicID;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
