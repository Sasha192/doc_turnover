package app.customtenant.eventdriven.domain;

import org.springframework.context.ApplicationEvent;

public abstract class GenericApplicationEvent<T>
        extends ApplicationEvent {

    private final T source;

    private final long occuredTime;

    private final String tenant;

    public GenericApplicationEvent(T source, String tenant) {
        super(source);
        this.source = source;
        occuredTime = System.currentTimeMillis();
        this.tenant = tenant;
    }

    public String getTenant() {
        return tenant;
    }

    public long getOccuredTime() {
        return occuredTime;
    }
}
