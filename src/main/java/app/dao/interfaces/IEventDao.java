package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.events.Event;
import app.models.events.PerformerEventAgent;
import java.sql.Date;
import java.util.List;

public interface IEventDao extends IGenericDao<Event> {

    List<Event> retrieveLastEvents();

    List<PerformerEventAgent> retrieveLastEventsForPerformerId(Long performerId);

    List<PerformerEventAgent> retrieveEventsForPerformerIdBeforeDate(Long performerId,
                                                                     Date beforeDate);

    List<PerformerEventAgent> retrieveEventsForPerformerIdAfterDate(Long performerId,
                                                                    Date afterDate);

    void seeEvent(Long eventId, Long performerId);

    int countNewEvents(Long performerId);
}
