package app.models.events;

import app.models.abstr.IdentityBaseEntity;
import app.models.basic.Performer;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "performer_id")
    protected Performer performer;

    @Transient
    /**
     *  Field:String name is used to represent description about event on frontend
     */
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

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
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

}
