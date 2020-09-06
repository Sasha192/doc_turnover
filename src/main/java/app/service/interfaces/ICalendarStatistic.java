package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.List;

public interface ICalendarStatistic
        extends IOperations<AbstractCalendarPerformerStatistic> {

    List<AbstractCalendarPerformerStatistic> findByPerformerId(Long id);
}
