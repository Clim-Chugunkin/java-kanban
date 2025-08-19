package manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Managers {
    private static final String fileName = "data.txt";

    public static TaskManager getDefault() {

        try {
            File file = new File(fileName);
            if (!file.exists()) Files.createFile(file.toPath());
            return FileBackedTaskManager.loadFromFile(file);
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
