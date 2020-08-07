package app.dao.impl;

import app.dao.interfaces.IEventDao;
import app.dao.persistance.GenericJpaRepository;
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
            " select evnt from Event ";

    private static final String RETRIEVE_LAST_10_EVENTS =
            FROM + " ORDER BY time_stamp DESC LIMIT 10 ";

    private static final String RETRIEVE_LAST_10_EVENTS_FOR_PERF_ID =
            FROM + " INNER JOIN performer_events pev ON pev.event_id = evnt.id "
                    + " WHERE pev.perf_id = :perf_id_ "
                    + " ORDER BY time_stamp DESC LIMIT 10 ";

    public EventDao() {
        setClazz(Event.class);
    }

    @Override
    public List<Event> retrieveLastEvents() {
        TypedQuery<Event> query = getEntityManager()
                .createQuery(RETRIEVE_LAST_10_EVENTS, Event.class);
        return query.getResultList();
    }

    @Override
    public List<Event> retrieveLastEventsForPerformerId(Long performerId) {
        return getEntityManager().createQuery(RETRIEVE_LAST_10_EVENTS_FOR_PERF_ID)
                .setParameter("perf_id_", performerId)
                .getResultList();
    }

    @Override
    @Deprecated
    /**
     * Not implemented yet
     */
    public List<Event> retrieveEventsForPerformerIdAfterDate(Long performerId, Date afterDate) {
        return null;
    }

    @Override
    @Deprecated
    /**
     * Not implemented yet
     */
    public List<Event> retrieveEventsForPerformerIdBeforeDate(Long performerId, Date beforeDate) {
        return null;
    }
}
