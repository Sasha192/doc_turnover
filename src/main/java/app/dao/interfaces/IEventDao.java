package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.events.Event;
import app.events.PerformerEventAgent;
import java.util.Date;
import java.util.List;

public interface IEventDao extends IGenericDao<Event> {

    List<Event> retrieveLastEvents();

    List<PerformerEventAgent> retrieveLastEventsForPerformerId(Long performerId);

    List<PerformerEventAgent> retrieveEventsForPerformerIdBeforeDate(Long performerId,
                                                                     Date beforeDate);

    List<PerformerEventAgent> retrieveEventsForPerformerIdAfterDate(Long performerId,
                                                                    Date afterDate);

    void seeEvent(Long eventId, Long performerId);

    Long countNewEvents(Long performerId);

    List<PerformerEventAgent> retrieveForPerformer(Long performerId);

    void seeAllEvents(Long performerId);

    List<PerformerEventAgent> retrieveAfterLastReceivedForPerformerId(
            Long id, Long lastReceivedEventId
    );
}
