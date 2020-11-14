package app.customtenant.models.jmsevents;

import app.customtenant.eventdriven.domain.TaskEventEnum;

public abstract class AbstractTaskJmsEvent {

    private Long taskId;

    private TaskEventEnum type;

    public AbstractTaskJmsEvent(Long taskId, TaskEventEnum type) {
        this.taskId = taskId;
        this.type = type;
    }

    public Long getTaskId() {
        return taskId;
    }

    public TaskEventEnum getType() {
        return type;
    }
}
