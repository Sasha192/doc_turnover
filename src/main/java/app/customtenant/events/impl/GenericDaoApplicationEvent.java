package app.customtenant.events.impl;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;
import app.customtenant.events.Event;

public class GenericDaoApplicationEvent
        extends GenericApplicationEvent<Event> {

    private Event.EventType type;

    public GenericDaoApplicationEvent(Event source,
                                      Event.EventType type,
                                      String tenant) {
        super(source, tenant);
        this.type = type;
    }

    public Event.EventType getType() {
        return type;
    }

    @Override
    public Event getSource() {
        return (Event) super.getSource();
    }
}
