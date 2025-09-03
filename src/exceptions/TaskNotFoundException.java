package exceptions;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException() {
        super("задачи нет");
    }
}
