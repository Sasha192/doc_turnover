package app.eventdriven.domain;

import app.models.basic.Task;

public class CreationTaskEvent extends TaskApplicationEvent {

    public CreationTaskEvent(Task task) {
        super(task);
    }

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.CREATION;
    }
}
