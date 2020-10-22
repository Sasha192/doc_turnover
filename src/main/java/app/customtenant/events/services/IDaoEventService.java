package app.customtenant.events.services;

import app.customtenant.eventdriven.service.IEventService;
import app.customtenant.events.impl.TaskEvent;

public interface IDaoEventService
        extends IEventService {

    TaskEvent.EventType getType();

}
