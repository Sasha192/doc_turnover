package app.eventdriven.service;

import app.eventdriven.domain.GenericApplicationEvent;

public interface IEventService {

    void service(GenericApplicationEvent event);

}
