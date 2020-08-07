package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.events.Event;
import java.sql.Date;
import java.util.List;

public interface IEventDao extends IGenericDao<Event> {

    List<Event> retrieveLastEvents();

    List<Event> retrieveLastEventsForPerformerId(Long performerId);

    List<Event> retrieveEventsForPerformerIdBeforeDate(Long performerId, Date beforeDate);

    List<Event> retrieveEventsForPerformerIdAfterDate(Long performerId, Date afterDate);
}
