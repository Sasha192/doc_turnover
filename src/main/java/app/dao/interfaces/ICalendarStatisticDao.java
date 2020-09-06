package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.statisticsmodule.domain.CalendarPerformerEnum;
import java.util.List;

public interface ICalendarStatisticDao
        extends IGenericDao<AbstractCalendarPerformerStatistic> {

    List<AbstractCalendarPerformerStatistic> findByPerformerId(Long id);

    List<AbstractCalendarPerformerStatistic> findAllByType(CalendarPerformerEnum type);
}
