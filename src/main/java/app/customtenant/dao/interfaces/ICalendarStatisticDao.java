package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.domain.CalendarPerformerEnum;
import java.util.List;

public interface ICalendarStatisticDao
        extends IGenericDao<AbstractCalendarPerformerStatistic> {

    List<AbstractCalendarPerformerStatistic> findByPerformerId(Long id);

    List<AbstractCalendarPerformerStatistic> findAllByType(CalendarPerformerEnum type);
}
