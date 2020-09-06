package app.service.interfaces;

import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.List;

public interface IPerformerStatisticCreation {

    List<AbstractCalendarPerformerStatistic> create(Long perfId);

}
