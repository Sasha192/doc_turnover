package app.customtenant.eventdriven.domain;

import app.customtenant.models.basic.taskmodels.Task;

public class TaskCreationEvent extends TaskApplicationEvent {

    public TaskCreationEvent(Task task, String tenant) {
        super(task, tenant);
    }

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.CREATION;
    }
}
