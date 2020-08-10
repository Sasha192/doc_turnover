package app.models.events;

import app.models.basic.Performer;
import app.models.mysqlviews.BriefPerformer;
import app.models.serialization.ExcludeForJsonEvent;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "performers_events")
public class PerformerEventAgent {

    @EmbeddedId
    @ExcludeForJsonEvent
    private PerformerEventId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perf_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    private Performer performer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perf_id", insertable = false, updatable = false)
    private BriefPerformer author;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("eventId")
    private Event event;

    @Column(name = "seen")
    private boolean seen;

    public PerformerEventId getId() {
        return id;
    }

    public void setId(PerformerEventId id) {
        this.id = id;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public BriefPerformer getAuthor() {
        return author;
    }

    public void setAuthor(BriefPerformer author) {
        this.author = author;
    }
}
