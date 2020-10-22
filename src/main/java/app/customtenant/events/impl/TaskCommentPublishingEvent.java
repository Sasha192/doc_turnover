package app.customtenant.events.impl;

import app.customtenant.events.Event;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "comment_event")
public class TaskCommentPublishingEvent extends Event {

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "task_id")
    private Long taskId;

    public TaskCommentPublishingEvent() {
        setEventTypeEnum(EventType.COMMENT_PUB);
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
