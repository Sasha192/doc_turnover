package app.customtenant.eventdriven.domain;

import app.customtenant.models.basic.taskmodels.Task;

public abstract class TaskApplicationEvent extends GenericApplicationEvent<Task> {

    protected TaskEventEnum type;

    public TaskApplicationEvent(Task task, String tenant) {
        super(task, tenant);
    }

    public abstract TaskEventEnum getType();
}
