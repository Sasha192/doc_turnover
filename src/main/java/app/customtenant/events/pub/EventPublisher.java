package app.customtenant.events.pub;

import app.customtenant.models.basic.Performer;

public interface EventPublisher<T> {

    void publish(T entity, Performer author);

}
