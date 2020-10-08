package app.customtenant.events.impl;

import app.customtenant.events.Event;
import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.serialization.ExcludeForJsonEvent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "comment_event")
public class TaskCommentPublishingEvent extends Event {

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    private TaskHolderComment comment;

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

    public TaskHolderComment getComment() {
        return comment;
    }

    @Deprecated
    /**
     * @see TaskCommentPublishingEvent#setCommentId(Long)
     *
     */
    public void setComment(TaskHolderComment comment) {
        this.comment = comment;
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
