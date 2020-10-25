package app.customtenant.eventdriven.domain;

import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;

public class TaskStatusModificationEvent extends TaskApplicationEvent {

    private final TaskStatus oldStatus;

    private final TaskStatus newStatus;

    public TaskStatusModificationEvent(Task task,
                                       TaskStatus oldStatus,
                                       TaskStatus newStatus,
                                       String tenant) {
        super(task, tenant);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.STATUS_MODIFICATION;
    }

    public TaskStatus getOldStatus() {
        return oldStatus;
    }

    public TaskStatus getNewStatus() {
        return newStatus;
    }
}
