import task.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        manager.addEpic(new Epic("Сделать уборку","убраться в комнате",Status.NEW));
        manager.addEpic(new Epic("Приготовиться к празднику","Приготовить все к празднику",Status.NEW));
        manager.addEpic(new Epic("Позвать гостей","позвать всех гостей",Status.NEW));

        for (Epic epic : manager.getAllEpic())
            System.out.println(epic);

        manager.addTask(new Task("task1 name","task1 description",Status.NEW));
        manager.addTask(new Task("task2 name","task2 description",Status.NEW));
        manager.addTask(new Task("task3 name","task3 description",Status.NEW));

        for (Task task : manager.getAllTask())
            System.out.println(task);

        manager.addSubTask(new Subtask("subtask1 name","subtask1 description",Status.NEW));
        manager.addSubTask(new Subtask("subtask2 name","subtask2 description",Status.NEW));
        manager.addSubTask(new Subtask("subtask3 name","subtask3 description",Status.NEW));

        for (Subtask subtask : manager.getAllSubTask())
            System.out.println(subtask);


    }
}
