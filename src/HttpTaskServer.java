import com.sun.net.httpserver.HttpServer;
import exceptions.IntersectedTaskException;
import handlers.*;
import manager.Managers;
import manager.TaskManager;


import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;
    private TaskManager manager = Managers.getInMemoryTaskManager();

    public static void main(String[] args) throws IOException, IntersectedTaskException {
        System.out.println("HttpTaskServer");
    }

    public void startServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/epics", new EpicsHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));

        httpServer.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public TaskManager getManager() {
        return manager;
    }

    public void stopServer() {
        httpServer.stop(0);
    }
}
