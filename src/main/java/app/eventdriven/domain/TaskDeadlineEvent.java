package app.eventdriven.domain;

import app.models.basic.Task;

public class TaskDeadlineEvent
        extends TaskApplicationEvent {

    public TaskDeadlineEvent(Task task) {
        super(task);
    }

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.DEADLINE_EXPIRED;
    }
}
