package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.EndpointNotFoundException;
import exceptions.IntersectedTaskException;
import exceptions.TaskNotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract  class BaseHttpHandler implements HttpHandler {
    protected final Gson gson = getJsonWithAdapters();

    private void sendResponse(HttpExchange exchange, String text, int code) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static Gson getJsonWithAdapters() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> sendResponse(exchange, doGet(exchange), 200);
                case "POST" -> sendResponse(exchange, doPost(exchange), 201);
                case "DELETE" -> sendResponse(exchange, doDelete(exchange), 200);
            }
        } catch (IntersectedTaskException ex) {
            sendResponse(exchange, ex.getMessage(), 406);
        } catch (TaskNotFoundException | EndpointNotFoundException ex) {
            sendResponse(exchange, ex.getMessage(), 404);
        }
    }

    protected String doGet(HttpExchange exchange) throws EndpointNotFoundException, IntersectedTaskException, TaskNotFoundException {
        throw new EndpointNotFoundException();
    }

    protected String doPost(HttpExchange exchange) throws EndpointNotFoundException, IntersectedTaskException, IOException {
        throw new EndpointNotFoundException();
    }

    protected String doDelete(HttpExchange exchange) throws EndpointNotFoundException, IntersectedTaskException, TaskNotFoundException {
        throw new EndpointNotFoundException();
    }
}
