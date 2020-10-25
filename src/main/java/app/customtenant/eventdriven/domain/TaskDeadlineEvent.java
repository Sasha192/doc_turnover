package app.customtenant.eventdriven.domain;

import app.customtenant.models.basic.taskmodels.Task;

public class TaskDeadlineEvent
        extends TaskApplicationEvent {

    public TaskDeadlineEvent(Task task, String tenant) {
        super(task, tenant);
    }

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.DEADLINE_EXPIRED;
    }
}
