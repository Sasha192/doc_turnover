package app.dao.impl;

import app.dao.interfaces.IEventDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.events.PerformerEventAgent;
import app.models.events.Event;
import java.sql.Date;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao
        extends GenericJpaRepository<Event>
        implements IEventDao {

    private static final String FROM =
            " select evnt from Event evnt ";

    private static final String RETRIEVE_LAST_EVENTS =
            FROM + " ORDER BY evnt.timeStamp DESC ";

    private static final String RETRIEVE_LAST_EVENTS_FOR_PERF_ID =
            " SELECT pev FROM PerformerEventAgent pev "
            + " INNER JOIN Event evnt ON evnt.id=pev.id.eventId "
            + " WHERE pev.id.performerId = :perf_id_ "
            + " ORDER BY pev.id.eventId DESC ";
    /*FROM + " INNER JOIN PerformerEventAgent pev ON pev.id.eventId = evnt.id "
                    + " WHERE pev.id.performerId = :perf_id_ "
                    + " ORDER BY evnt.timeStamp DESC";*/

    public EventDao() {
        setClazz(Event.class);
    }

    @Override
    public List<Event> retrieveLastEvents() {
        TypedQuery<Event> query = getEntityManager()
                .createQuery(RETRIEVE_LAST_EVENTS, Event.class);
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public List<PerformerEventAgent> retrieveLastEventsForPerformerId(Long performerId) {
        return getEntityManager()
                .createQuery(RETRIEVE_LAST_EVENTS_FOR_PERF_ID, PerformerEventAgent.class)
                .setParameter("perf_id_", performerId)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    @Deprecated
    /**
     * Not implemented yet
     */
    public List<PerformerEventAgent> retrieveEventsForPerformerIdAfterDate(Long performerId,
                                                                           Date afterDate) {
        return null;
    }

    @Override
    @Deprecated
    /**
     * Not implemented yet
     */
    public List<PerformerEventAgent> retrieveEventsForPerformerIdBeforeDate(Long performerId,
                                                                            Date beforeDate) {
        return null;
    }
}
