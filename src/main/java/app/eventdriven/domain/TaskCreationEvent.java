package app.eventdriven.domain;

import app.models.basic.Task;

public class TaskCreationEvent extends TaskApplicationEvent {

    public TaskCreationEvent(Task task) {
        super(task);
    }

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.CREATION;
    }
}
