package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.events.Event;
import java.sql.Date;
import java.util.List;

public interface IEventService extends IOperations<Event> {

    List<Event> retrieveLastEvents();

    List<Event> retrieveLastEventsForPerformerId(Long performerId);

    @Deprecated
    /**
     * Not implemented yet
     */
    List<Event> retrieveEventsForPerformerIdAfterDate(Long performerId, Date afterDate);

    @Deprecated
    /**
     * Not implemented yet
     */
    List<Event> retrieveEventsForPerformerIdBeforeDate(Long performerId, Date beforeDate);
}
