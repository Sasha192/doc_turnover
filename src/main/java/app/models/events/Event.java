package app.models.events;

import app.models.abstr.IdentityBaseEntity;
import app.models.basic.Performer;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
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
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
@Table(name = "app_events")
public abstract class Event
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "time_stamp")
    protected Timestamp timeStamp;

    @Column(name = "date")
    protected Date date;

    @Column(name = "time")
    protected Time time;

    @Column(name = "event_type_enum")
    @Enumerated(EnumType.ORDINAL)
    protected EventType eventTypeEnum;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    protected Performer publisher;

    /**
     * Long[] performersId stores performers Id, for whom to show this event
     * as notification about origin of event
     */
    @ElementCollection
    @CollectionTable(name = "performers_events",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "performer_id")
    protected Set<Long> performersId;

    /**
     *  Field:String name is used to represent description about event on frontend
     */
    @Transient
    protected String description;

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timeStamp = timestamp;
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

    public EventType getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(EventType eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Performer getPublisher() {
        return publisher;
    }

    public void setPublisher(Performer publisher) {
        this.publisher = publisher;
    }

    public String getName() {
        return description;
    }

    public void setName(String description) {
        this.description = description;
    }

    public enum EventType {
        COMMENT_PUB, REPORT_PUB, DOC_PUB, TASK_PUB
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Long> getPerformersId() {
        return performersId;
    }

    public void setPerformersId(Set<Long> performersId) {
        this.performersId = performersId;
    }
}
