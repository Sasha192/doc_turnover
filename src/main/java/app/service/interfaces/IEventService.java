package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.events.Event;
import app.models.events.PerformerEventAgent;
import java.sql.Date;
import java.util.List;

public interface IEventService extends IOperations<Event> {

    List<Event> retrieveLastEvents();

    List<PerformerEventAgent> retrieveLastEventsForPerformerId(Long performerId);

    void seeEvent(Long eventId, Long performerId);

    List<PerformerEventAgent> retrieveForPerformer(Long performerId);

    @Deprecated
    /**
     * Not implemented yet
     */
    List<PerformerEventAgent> retrieveEventsForPerformerIdAfterDate(Long performerId,
                                                                    Date afterDate);

    @Deprecated
    /**
     * Not implemented yet
     */
    List<PerformerEventAgent> retrieveEventsForPerformerIdBeforeDate(Long performerId,
                                                                     Date beforeDate);

    Long countNewEvents(Long performerId);

    void seeAllEvents(Long performerId);
}
