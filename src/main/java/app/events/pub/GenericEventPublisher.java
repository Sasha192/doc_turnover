package app.events.pub;

import app.service.interfaces.IEventService;

public abstract class GenericEventPublisher<T>
        implements EventPublisher<T> {

    private IEventService eventService;

    public GenericEventPublisher(IEventService eventService) {
        this.eventService = eventService;
    }

    public IEventService getEventService() {
        return eventService;
    }

    public final void setEventService(IEventService eventService) {
        this.eventService = eventService;
    }
}
