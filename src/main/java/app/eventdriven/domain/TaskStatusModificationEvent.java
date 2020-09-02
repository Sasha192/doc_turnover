package app.eventdriven.domain;

import app.models.basic.Task;
import app.models.basic.TaskStatus;

public class TaskStatusModificationEvent extends TaskApplicationEvent {

    private final TaskStatus oldStatus;

    private final TaskStatus newStatus;

    public TaskStatusModificationEvent(Task task,
                                       TaskStatus oldStatus,
                                       TaskStatus newStatus) {
        super(task);
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
