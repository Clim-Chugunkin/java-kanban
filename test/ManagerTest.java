import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class ManagerTest {
    @Test
    void checkClassInstance() {
        TaskManager manager = Managers.getDefault();
        HistoryManager hManager = Managers.getDefaultHistory();
        assertNotNull(manager);
        assertNotNull(hManager);
    }
}
