package app.customtenant.events.impl;

import app.customtenant.events.Event;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "deadline_pub_event")
public class DeadlinePublishingEvent
        extends Event {

    @Column(name = "task_id")
    private Long taskId;

    public DeadlinePublishingEvent(String message) {
        this();
        setMessage(message);
    }

    public DeadlinePublishingEvent() {
        setEventTypeEnum(EventType.DEADLINE_NOTIFICATION);
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
