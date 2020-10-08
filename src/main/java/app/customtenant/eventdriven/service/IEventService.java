package app.customtenant.eventdriven.service;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;

public interface IEventService {

    void service(GenericApplicationEvent event);

}
