import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

class TaskTest {


    //проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void checkEqualsTaskById(){
        Task task1 = new Task("task1", "task1 description", NEW);

        task1.setTaskID(1);
        Task task2  = new Task("task2","task2 description",NEW);

        task2.setTaskID(1);
        assertEquals(task1,task2,"Разные задачи ");
    }

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void checkEqualsEpicById(){
        Epic epic1 = new Epic("epic1", "epic1 description", NEW);

        epic1.setTaskID(1);
        Epic epic2 = new Epic("epic2","epic2 description",NEW);

        epic2.setTaskID(1);
        assertEquals(epic1,epic2,"Разные эпики ");
    }


}