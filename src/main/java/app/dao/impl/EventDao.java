package app.dao.impl;

import app.dao.interfaces.IEventDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.events.Event;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class EventDao
        extends GenericJpaRepository<Event>
        implements IEventDao {

    private static final String FROM =
            " select evnt from Event ";

    private static final String RETRIEVE_LAST_10_EVENTS =
            FROM + " ORDER BY time_stamp DESC LIMIT 10 ";

    public EventDao() {
        setClazz(Event.class);
    }

    @Override
    public List<Event> retrieveLastEvents() {
        TypedQuery<Event> query = getEntityManager()
                .createQuery(RETRIEVE_LAST_10_EVENTS, Event.class);
        return query.getResultList();
    }
}
