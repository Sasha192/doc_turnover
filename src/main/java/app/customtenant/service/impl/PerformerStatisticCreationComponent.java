package app.customtenant.service.impl;

import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.service.interfaces.IPerformerStatisticCreation;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.domain.AllPerformerStatistic;
import app.customtenant.statisticsmodule.domain.AnnuallyPerformerStatistics;
import app.customtenant.statisticsmodule.domain.MonthlyPerformerStatistic;
import app.customtenant.statisticsmodule.domain.WeeklyPerformerStatistics;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerformerStatisticCreationComponent
        implements IPerformerStatisticCreation {

    @Autowired
    private ICalendarStatistic statistic;

    @Override
    public List<AbstractCalendarPerformerStatistic> create(Long perfId) {
        WeeklyPerformerStatistics weekly = new WeeklyPerformerStatistics();
        weekly.setPerformerId(perfId);
        statistic.create(weekly);
        MonthlyPerformerStatistic monthly = new MonthlyPerformerStatistic();
        monthly.setPerformerId(perfId);
        statistic.create(monthly);
        AnnuallyPerformerStatistics annually = new AnnuallyPerformerStatistics();
        annually.setPerformerId(perfId);
        statistic.create(annually);
        AllPerformerStatistic allStat = new AllPerformerStatistic();
        allStat.setPerformerId(perfId);
        statistic.create(allStat);
        List<AbstractCalendarPerformerStatistic> statistics = new LinkedList<>();
        statistics.add(weekly);
        statistics.add(monthly);
        statistics.add(annually);
        statistics.add(allStat);
        return statistics;
    }
}
