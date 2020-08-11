package app.models.events.impl;

import app.models.abstr.Comment;
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
@DiscriminatorValue(value = "comment_event")
public class CommentPublishingEvent extends Event {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    private Comment commentEntity;

    @Column(name = "comment_id")
    private Long commentId;

    public CommentPublishingEvent() {
        setEventTypeEnum(EventType.COMMENT_PUB);
    }

    public Comment getCommentEntity() {
        return commentEntity;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
