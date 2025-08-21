import manager.InMemoryTaskManager;
import exceptions.IntersectedTaskException;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;


import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    //TaskManager taskManager;

    @BeforeEach
    public void setUP() {
        taskManager = new InMemoryTaskManager();
    }


}