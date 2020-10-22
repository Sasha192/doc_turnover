package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.events.Event;
import java.util.List;

public interface IEventService extends IOperations<Event> {

    List<Event> retrieveForPerformer(int page, Long performerId);

    Long countNewEvents(Long performerId);

    void seeAllEvents(Long performerId);
}
