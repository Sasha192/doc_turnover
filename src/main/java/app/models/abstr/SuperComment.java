package app.models.abstr;

import app.models.basic.Performer;
import app.models.mysqlviews.BriefPerformer;
import app.models.serialization.ExcludeForJsonComment;
import app.utils.CustomAppDateTimeUtil;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@MappedSuperclass
public abstract class SuperComment
        extends IdentityBaseEntity {

    protected transient Set<Long> performerIds;

    @Column(name = "comment")
    protected String comment;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", insertable = false, updatable = false)
    @ExcludeForJsonComment
    protected Performer author;

    @Column(name = "performer_id")
    protected Long authorId;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "performer_id", insertable = false, updatable = false)
    protected BriefPerformer performer;

    @Column(name = "date")
    protected Date date;

    @Column(name = "time")
    protected Time time;

    @Column(name = "date_time")
    protected transient Timestamp dateTime;

    public SuperComment() {
        this.date = CustomAppDateTimeUtil.now();
        this.time = Time.valueOf(LocalTime.now());
    }

    @Deprecated
    /**
     * @see SuperComment#setAuthorId(Long)
     */
    public void setAuthor(Performer author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public BriefPerformer getPerformer() {
        return performer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * @return Set of Performer Ids, that will see this comment on frontend part Notifications
     * @see app.events.pub.CommentEventPublisher#publish(TaskHolderComment, Performer)
     */
    public abstract Set<Long> getPerformerIds();

    public void setPerformerIds(Set<Long> performerIds) {
        this.performerIds = performerIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TaskHolderComment)) {
            return false;
        }

        TaskHolderComment taskHolderComment = (TaskHolderComment) o;

        return new EqualsBuilder()
                .append(getId(), taskHolderComment.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .toHashCode();
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public Performer getAuthor() {
        return author;
    }

}
