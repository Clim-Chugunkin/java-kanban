package exceptions;

public class EndpointNotFoundException extends Exception {
    public EndpointNotFoundException() {
        super("нет такого эндпоинта");
    }
}
