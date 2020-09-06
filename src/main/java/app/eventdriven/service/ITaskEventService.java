package app.eventdriven.service;

import app.eventdriven.domain.TaskEventEnum;

public interface ITaskEventService<T>
        extends IEventService {

    /**
     * @return TaskEventEnum SERVICED EVENT TYPE :
     * What is appropriate Event Type for this Service ?
     * @see app.eventdriven.listeners.TaskApplicationEventListener
     * #TaskApplicationEventListener(List)
     */
    TaskEventEnum getType();

}
