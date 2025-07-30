package task;

import static task.TaskTypes.EPIC;
import static task.TaskTypes.TASK;

public class Epic extends Task {

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(String[] str) {
        super(str);
    }

    @Override
    public String getString() {
        return super.getString().replace(TASK.name(), EPIC.name());
    }
}
