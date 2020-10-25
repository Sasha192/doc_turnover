package app.customtenant.service.impl;

import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.service.interfaces.IPerformerStatisticCreation;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
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
        DailyPerformerStatistic daily = new DailyPerformerStatistic();
        daily.setPerformerId(perfId);
        statistic.create(daily);
        WeeklyPerformerStatistics weekly = new WeeklyPerformerStatistics();
        weekly.setPerformerId(perfId);
        statistic.create(weekly);
        MonthlyPerformerStatistic monthly = new MonthlyPerformerStatistic();
        monthly.setPerformerId(perfId);
        statistic.create(monthly);
        AnnuallyPerformerStatistics annually = new AnnuallyPerformerStatistics();
        annually.setPerformerId(perfId);
        statistic.create(annually);
        List<AbstractCalendarPerformerStatistic> statistics = new LinkedList<>();
        statistics.add(daily);
        statistics.add(weekly);
        statistics.add(monthly);
        statistics.add(annually);
        return statistics;
    }
}
