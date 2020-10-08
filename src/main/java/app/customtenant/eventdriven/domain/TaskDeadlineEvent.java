package app.customtenant.eventdriven.domain;

import app.customtenant.models.basic.taskmodels.Task;

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
