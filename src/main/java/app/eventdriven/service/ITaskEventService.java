package app.eventdriven.service;

import app.eventdriven.domain.TaskEventEnum;

public interface ITaskEventService<T>
        extends IEventService {

    TaskEventEnum getType();

}
