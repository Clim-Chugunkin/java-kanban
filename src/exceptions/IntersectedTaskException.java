package exceptions;

public class IntersectedTaskException extends Exception {
    public IntersectedTaskException() {
        super("Пересечение задач");
    }
}
