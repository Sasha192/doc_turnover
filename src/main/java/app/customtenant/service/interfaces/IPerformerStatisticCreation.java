package app.customtenant.service.interfaces;

import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.List;

public interface IPerformerStatisticCreation {

    List<AbstractCalendarPerformerStatistic> create(Long perfId);

}
