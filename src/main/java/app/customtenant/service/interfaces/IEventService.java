package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.events.Event;
import app.customtenant.events.PerformerEventAgent;
import java.util.Date;
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

    List<PerformerEventAgent> retrieveAfterLastReceivedForPerformerId(
            Long id, Long lastReceivedEventId
    );
}
