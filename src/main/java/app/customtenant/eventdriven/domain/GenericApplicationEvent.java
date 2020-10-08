package app.customtenant.eventdriven.domain;

import org.springframework.context.ApplicationEvent;

public abstract class GenericApplicationEvent<T>
        extends ApplicationEvent {

    private final T source;

    private final long occuredTime;

    public GenericApplicationEvent(T source) {
        super(source);
        this.source = source;
        occuredTime = System.currentTimeMillis();
    }

    public long getOccuredTime() {
        return occuredTime;
    }
}
