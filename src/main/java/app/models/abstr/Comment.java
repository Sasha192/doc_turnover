package app.models.abstr;

import app.models.basic.Performer;
import app.models.mysqlviews.BriefPerformer;
import app.models.serialization.ExcludeForJsonComment;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "comment_type")
@Table(name = "comment_post")
public abstract class Comment
        extends IdentityBaseEntity {
    // @TODO : inheritance from IdentityBaseEntity

    @Transient
    protected Set<Long> performerIds;

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

    public Comment() {
        this.date = Date.valueOf(LocalDate.now());
        this.time = Time.valueOf(LocalTime.now());
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
     *
     * @return Set of Performer Ids, that will see this comment on frontend part Notifications
     * @see app.models.events.pub.CommentEventPublisher#publish(Comment, Performer)
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

        if (!(o instanceof Comment)) {
            return false;
        }

        Comment comment = (Comment) o;

        return new EqualsBuilder()
                .append(getId(), comment.getId())
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
