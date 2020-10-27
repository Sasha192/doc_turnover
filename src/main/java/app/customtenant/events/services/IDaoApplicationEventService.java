package app.customtenant.events.services;

import app.customtenant.eventdriven.service.IApplicationEventService;
import app.customtenant.events.impl.TaskEvent;

public interface IDaoApplicationEventService
        extends IApplicationEventService {

    TaskEvent.EventType getType();

}
