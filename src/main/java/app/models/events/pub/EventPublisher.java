package app.models.events.pub;

import app.models.basic.Performer;

public interface EventPublisher<T> {

    void publish(T entity, Performer author);

}
