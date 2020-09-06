package app.dao.impl;

import app.dao.interfaces.ICalendarStatisticDao;
import app.dao.persistance.GenericJpaRepository;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.List;

public class CalendarStatisticDao
        extends GenericJpaRepository<AbstractCalendarPerformerStatistic>
        implements ICalendarStatisticDao {

    private static final String FIND_BY_PERF =
            "SELECT stat FROM AbstractCalendarPerformerStatistic "
            + " WHERE stat.performerId = :perf_id_ ";

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
}
