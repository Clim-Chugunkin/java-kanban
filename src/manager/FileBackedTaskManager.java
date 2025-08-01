package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskTypes;

import java.io.*;

import static task.TaskTypes.TASK;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String fileName;

    //Пусть новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле
    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    private void save() throws ManagerSaveException {
        //открываем поток для записи в фаил
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
            //записываем все задачи
            for (Task task : getAllTask())
                fileWriter.write(task.getString() + "\n");
            //записываем все эпики
            for (Epic epic : getAllEpic())
                fileWriter.write(epic.getString() + "\n");

            //записываем все подзадачи
            for (Subtask subtask : getAllSubTask())
                fileWriter.write(subtask.getString() + "\n");
        } catch (IOException ex) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }

    }

    public void loadFromFile() throws ManagerSaveException {
        //открываем фаил для ятения
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String[] stringToParse;
            while (fileReader.ready()) {
                stringToParse = fileReader.readLine().split(",");
                switch (TaskTypes.valueOf(stringToParse[1])) {
                    case TASK -> super.addTask(new Task(stringToParse));

                    case EPIC -> super.addEpic(new Epic(stringToParse));

                    case SUBTASK -> super.addSubTask(new Subtask(stringToParse));
                }
            }

        } catch (IOException ex) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getPath());
        manager.loadFromFile();
        return manager;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }

    @Override
    public int addSubTask(Subtask subtask) {
        int id = super.addSubTask(subtask);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void clearSubtask() {
        super.clearSubtask();
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void eraseTaskByID(int id) {
        super.eraseTaskByID(id);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void eraseEpicByID(int id) {
        super.eraseEpicByID(id);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void eraseSubtaskByID(int id) {
        super.eraseSubtaskByID(id);
        try {
            save();
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
