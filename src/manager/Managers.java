package manager;

import exceptions.IntersectedTaskException;
import exceptions.ManagerSaveException;

import java.io.IOException;
import java.nio.file.Files;

public class Managers {

    public static TaskManager getDefault() {
        try {
            return FileBackedTaskManager.loadFromFile(Files.createTempFile("out", "txt").toFile());
        } catch (ManagerSaveException | IOException | IntersectedTaskException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
