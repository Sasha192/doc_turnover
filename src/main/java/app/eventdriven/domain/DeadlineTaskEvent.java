package app.eventdriven.domain;

import app.models.basic.Task;

public class DeadlineTaskEvent
        extends TaskApplicationEvent {

    public DeadlineTaskEvent(Task task) {
        super(task);
    }

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.DEADLINE_EXPIRED;
    }
}
