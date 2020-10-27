package app.customtenant.eventdriven.service;

import app.customtenant.eventdriven.domain.TaskEventEnum;

public interface ITaskApplicationEventService<T>
        extends IApplicationEventService {

    /**
     * @return TaskEventEnum SERVICED EVENT TYPE :
     * What is appropriate Event Type for this Service ?
     * @see app.customtenant.eventdriven.listeners.TaskApplicationEventListener
     * #TaskApplicationEventListener(List)
     */
    TaskEventEnum getType();

}
