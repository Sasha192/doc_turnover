package app.eventdriven.domain;

import app.models.basic.taskmodels.Task;

public abstract class TaskApplicationEvent extends GenericApplicationEvent<Task> {

    protected TaskEventEnum type;

    public TaskApplicationEvent(Task task) {
        super(task);
    }

    public abstract TaskEventEnum getType();
}
