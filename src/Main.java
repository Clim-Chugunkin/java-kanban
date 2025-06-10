import task.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        manager.addEpic(new Epic("epic1 name","epic1 description",Status.NEW));
        manager.addEpic(new Epic("epic2 name","epic2 description",Status.NEW));
        manager.addEpic(new Epic("epic3 name","epic3 description",Status.NEW));

        for (Epic epic : manager.getAllEpic())
            System.out.println(epic);

        manager.addTask(new Task("task1 name","task1 description",Status.NEW));
        manager.addTask(new Task("task2 name","task2 description",Status.NEW));
        manager.addTask(new Task("task3 name","task3 description",Status.NEW));

        for (Task task : manager.getAllTask())
            System.out.println(task);

        manager.addSubTask(new Subtask("subtask1 name","subtask1 description",Status.NEW,1));
        manager.addSubTask(new Subtask("subtask2 name","subtask2 description",Status.NEW,1));
        manager.addSubTask(new Subtask("subtask3 name","subtask3 description",Status.NEW,2));

        for (Subtask subtask : manager.getAllSubTask())
            System.out.println(subtask);
        System.out.println("epic1 subtasks : ");
        System.out.println(manager.getAllEpicSubtask(1));

        System.out.println("epic2 subtasks : ");
        System.out.println(manager.getAllEpicSubtask(2));

        System.out.println("epic3 subtasks : ");
        System.out.println(manager.getAllEpicSubtask(3));

        System.out.println("epic1 status : ");
        System.out.println(manager.getEpicByID(1).getStatus());

        manager.getSubtaskByID(7).setStatus(Status.IN_PROGRESS);
        System.out.println("epic1 status : ");
        Subtask subtask = manager.getSubtaskByID(7);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        System.out.println(manager.getEpicByID(1).getStatus());
        for (Subtask subtask1 : manager.getAllSubTask()){
            if (subtask1.getEpicID() == 1){
                subtask = subtask1;
                subtask.setStatus(Status.DONE);
                manager.updateSubtask(subtask);
            }
        }
        System.out.println(manager.getEpicByID(1).getStatus());
        manager.eraseEpicByID(1);
        System.out.println("subtask after epic deleted");
        System.out.println(manager.getAllSubTask());
    }
}
