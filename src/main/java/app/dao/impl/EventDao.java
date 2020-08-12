package app.dao.impl;

import app.dao.interfaces.IEventDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.events.Event;
import app.models.events.PerformerEventAgent;
import java.sql.Date;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao
        extends GenericJpaRepository<Event>
        implements IEventDao {

    private static final String NEW_COUNT =
            " SELECT COUNT(*) from PerformerEventAgent pev "
            + " WHERE pev.id.performerId = :perf_id_ "
            + " AND seen = false";

    private static final String FROM =
            " select evnt from Event evnt ";

    private static final String RETRIEVE_LAST_EVENTS =
            FROM + " ORDER BY evnt.timeStamp DESC ";

    private static final String SEE_ALL_EVENTS_FOR_PERFORMER =
            " UPDATE PerformerEventAgent evnt SET evnt.seen=true "
                    + "WHERE evnt.id.performerId=:performer_id_ ";

    private static final String SEE_EVENT_FOR_PERFORMER =
            SEE_ALL_EVENTS_FOR_PERFORMER + " AND evnt.id.eventId = :event_id_ ";

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
    public void seeEvent(Long eventId, Long performerId) {
        getEntityManager().createQuery(SEE_EVENT_FOR_PERFORMER)
                .setParameter("performer_id_", performerId)
                .setParameter("event_id_", eventId)
                .executeUpdate();
    }

    @Override
    public Long countNewEvents(Long performerId) {
        return (Long)getEntityManager().createQuery(NEW_COUNT)
                .setParameter("perf_id_", performerId)
                .getSingleResult();
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

    @Override
    public List<PerformerEventAgent> retrieveForPerformer(Long performerId) {
        return getEntityManager()
                .createQuery(RETRIEVE_LAST_EVENTS_FOR_PERF_ID, PerformerEventAgent.class)
                .setParameter("perf_id_", performerId)
                .getResultList();
    }

    @Override
    public void seeAllEvents(Long performerId) {
        getEntityManager()
                .createQuery(SEE_ALL_EVENTS_FOR_PERFORMER)
                .setParameter("performer_id_", performerId)
                .executeUpdate();
    }
}
