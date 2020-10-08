package app.customtenant.eventdriven.service;

import app.customtenant.eventdriven.domain.TaskEventEnum;

public interface ITaskEventService<T>
        extends IEventService {

    /**
     * @return TaskEventEnum SERVICED EVENT TYPE :
     * What is appropriate Event Type for this Service ?
     * @see app.customtenant.eventdriven.listeners.TaskApplicationEventListener
     * #TaskApplicationEventListener(List)
     */
    TaskEventEnum getType();

}
