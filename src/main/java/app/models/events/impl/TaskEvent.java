package app.models.events.impl;

import app.models.basic.Task;
import app.models.events.Event;
import app.models.serialization.ExcludeForJsonEvent;
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

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
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
