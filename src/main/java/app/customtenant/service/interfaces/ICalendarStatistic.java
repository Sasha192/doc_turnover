package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.domain.CalendarPerformerEnum;
import java.util.List;

public interface ICalendarStatistic
        extends IOperations<AbstractCalendarPerformerStatistic> {

    List<AbstractCalendarPerformerStatistic> findByPerformerId(Long perfId);

    List<AbstractCalendarPerformerStatistic> findAllByType(CalendarPerformerEnum type);
}
