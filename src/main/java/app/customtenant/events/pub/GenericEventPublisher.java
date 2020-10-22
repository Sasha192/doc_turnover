package app.customtenant.events.pub;

import org.springframework.context.ApplicationEventPublisher;

public abstract class GenericEventPublisher<T>
        implements EventPublisher<T> {

    private final ApplicationEventPublisher eventPublisher;

    public GenericEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }
}
