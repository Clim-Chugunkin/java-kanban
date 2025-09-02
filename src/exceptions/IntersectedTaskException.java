package exceptions;

public class IntersectedTaskException extends Exception {
    public IntersectedTaskException() {
        super("задачи пересекается с существующими");
    }
}
