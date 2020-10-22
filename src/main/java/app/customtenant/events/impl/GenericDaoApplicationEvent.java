package app.customtenant.events.impl;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;
import app.customtenant.events.Event;

public class GenericDaoApplicationEvent
        extends GenericApplicationEvent<Event> {

    private Event.EventType type;

    private String tenant;

    public GenericDaoApplicationEvent(Event source, Event.EventType type, String tenant) {
        super(source);
        this.type = type;
        this.tenant = tenant;
    }

    public Event.EventType getType() {
        return type;
    }

    public String getTenant() {
        return tenant;
    }

    @Override
    public Event getSource() {
        return (Event) super.getSource();
    }
}
