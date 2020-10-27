package app.customtenant.eventdriven.service;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;

public interface IApplicationEventService {

    void service(GenericApplicationEvent event);

}
