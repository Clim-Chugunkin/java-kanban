import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

public class ManagerTest {
    @Test
    void checkClassInstance() {
        TaskManager manager = Managers.getDefault();
        HistoryManager hManager = Managers.getDefaultHistory();

        assertNotNull(manager);
        assertNotNull(hManager);
    }


}
