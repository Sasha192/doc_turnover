package app.dao.impl;

import app.dao.interfaces.ICalendarStatisticDao;
import app.dao.persistance.GenericJpaRepository;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.statisticsmodule.domain.CalendarPerformerEnum;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CalendarStatisticDao
        extends GenericJpaRepository<AbstractCalendarPerformerStatistic>
        implements ICalendarStatisticDao {

    private static String FROM =
            " from ";

    private static final String FIND_BY_PERF =
            "FROM AbstractCalendarPerformerStatistic "
            + " WHERE performerId = :perf_id_ ";

    public CalendarStatisticDao() {
        setClazz(AbstractCalendarPerformerStatistic.class);
    }

    @Override
    public List<AbstractCalendarPerformerStatistic> findByPerformerId(Long id) {
        return getEntityManager()
                .createQuery(FIND_BY_PERF, AbstractCalendarPerformerStatistic.class)
                .setParameter("perf_id_", id)
                .getResultList();
    }

    @Override
    public List<AbstractCalendarPerformerStatistic> findAllByType(
            CalendarPerformerEnum type) {
        String query = FROM + type.getClazz().getName();
        return (List<AbstractCalendarPerformerStatistic>) getEntityManager()
                .createQuery(query, type.getClazz())
                .getResultList();
    }
}
