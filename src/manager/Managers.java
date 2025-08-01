package manager;

import java.io.File;
import java.io.IOException;

public class Managers {


    public static TaskManager getDefault() {
        try {
            return FileBackedTaskManager.loadFromFile(File.createTempFile("out", ".txt"));
        } catch (ManagerSaveException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
