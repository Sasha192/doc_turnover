package app.customtenant.events;

import app.customtenant.models.abstr.IdentityBaseEntity;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeForJsonEvent;
import app.utils.CustomAppDateTimeUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
@Table(name = "app_event")
public abstract class Event
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "creation_time")
    protected long creationTime;

    @Column(name = "date")
    protected Date date;

    @Column(name = "event_type_enum")
    @Enumerated(EnumType.ORDINAL)
    protected EventType eventTypeEnum;

    @Column(name = "event_message")
    protected String message;

    @Column(name = "performer_id")
    protected Long authorId;

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    protected Performer authorPerformer;

    /**
     * Long[] performersId stores performers Id, for whom to show this event
     * as notification about origin of event
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "performers_events",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "perf_id")
    @ExcludeForJsonEvent
    protected Set<Long> performersId;

    public Event() {
        this.date = CustomAppDateTimeUtil.now();;
        this.creationTime = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Performer getAuthorPerformer() {
        return authorPerformer;
    }

    public Set<Long> getPerformersId() {
        return performersId;
    }

    public void setPerformersId(Set<Long> performersId) {
        this.performersId = performersId;
    }

    public EventType getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(EventType eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }

    public enum EventType {
        COMMENT_PUB, REPORT_PUB, TASK_PUB, DEADLINE_NOTIFICATION, CONTROL_DATE_NOTIFICATION, TASK_STATUS_MODIFICATION;
    }
}
