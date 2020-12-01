package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.IEventDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.events.Event;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao
        extends GenericJpaRepository<Event>
        implements IEventDao {

    private static final String NEW_COUNT =
            " SELECT COUNT(*) from PerformerEventAgent pev "
                    + " WHERE pev.id.performerId = :perf_id_ "
                    + " AND seen = false";

    private static final String SEE_ALL_EVENTS_FOR_PERFORMER =
            " UPDATE PerformerEventAgent evnt SET evnt.seen=true "
                    + "WHERE evnt.id.performerId=:performer_id_ ";

    private static final String FIND_FOR_PERF =
            "SELECT id, time_stamp, date, creation_time, event_type_enum, event_type, " +
                    " performer_id, comment_id, report_id, doc_id, task_id, event_message, task_name "
                    + "FROM app_event ev "
                    + "INNER JOIN performers_events pe on ev.id = pe.event_id "
                    + "WHERE pe.perf_id = :perf_id "
                    + " ORDER BY ev.id ";

    public EventDao() {
        setClazz(Event.class);
    }

    @Override
    public Long countNewEvents(Long performerId) {
        return (Long)getEntityManager().createQuery(NEW_COUNT)
                .setParameter("perf_id_", performerId)
                .getSingleResult();
    }

    @Override
    public List<Event> retrieveForPerformer(int page, int pageSize, Long performerId) {
        return getEntityManager().createNativeQuery(FIND_FOR_PERF, Event.class)
                .setParameter("perf_id", performerId)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
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
