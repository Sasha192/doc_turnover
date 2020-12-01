package app.customtenant.events.impl;

import app.customtenant.events.Event;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "task_event")
public class TaskEvent extends Event {

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_name")
    private String taskName;

    public TaskEvent(String taskName) {
        this();
        this.taskName = taskName;
    }

    public TaskEvent() {
        setEventTypeEnum(EventType.TASK_PUB);
        setMessage("Створено задачу");
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
