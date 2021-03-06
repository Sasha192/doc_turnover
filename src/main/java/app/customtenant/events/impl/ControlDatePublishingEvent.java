package app.customtenant.events.impl;

import app.customtenant.events.Event;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "control_date_pub_event")
public class ControlDatePublishingEvent
        extends Event {

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_id")
    private Long taskId;

    public ControlDatePublishingEvent(String message, String taskName) {
        this();
        this.taskName = taskName;
        setMessage(message);
    }

    public ControlDatePublishingEvent() {
        setEventTypeEnum(EventType.CONTROL_DATE_NOTIFICATION);
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
