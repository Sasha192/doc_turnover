package app.customtenant.events.impl;

import app.customtenant.events.Event;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.models.serialization.ExcludeForJsonEvent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "task_event")
public class TaskEvent extends Event {

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    private Task task;

    @Column(name = "task_id")
    private Long taskId;

    public TaskEvent() {
        setEventTypeEnum(EventType.TASK_PUB);
    }

    public Task getTask() {
        return task;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
