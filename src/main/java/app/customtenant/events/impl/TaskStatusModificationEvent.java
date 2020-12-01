package app.customtenant.events.impl;

import app.customtenant.events.Event;
import app.customtenant.models.basic.TaskStatus;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "task_status")
public class TaskStatusModificationEvent
        extends Event {

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_name")
    private String taskName;

    public TaskStatusModificationEvent(String taskName, Long taskId, TaskStatus newStatus) {
        this();
        this.taskId = taskId;
        this.taskName = taskName;
        this.message = newStatus.getMessage();
    }

    public TaskStatusModificationEvent() {
        setEventTypeEnum(EventType.TASK_STATUS_MODIFICATION);
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
