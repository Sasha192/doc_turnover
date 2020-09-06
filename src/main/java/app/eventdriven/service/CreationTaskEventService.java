package app.eventdriven.service;

import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskCreationEvent;
import app.eventdriven.domain.TaskEventEnum;
import app.models.basic.Task;
import app.service.interfaces.ICalendarStatistic;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.statisticsmodule.domain.AnnuallyPerformerStatistics;
import app.statisticsmodule.domain.DailyPerformerStatistic;
import app.statisticsmodule.domain.MonthlyPerformerStatistic;
import app.statisticsmodule.domain.WeeklyPerformerStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreationTaskEventService
        implements ITaskEventService<TaskCreationEvent> {

    @Autowired
    private ICalendarStatistic statistic;

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.CREATION;
    }

    @Override
    @Transactional
    public void service(GenericApplicationEvent gevent) {
        if (!(gevent instanceof TaskCreationEvent)) {
            return;
        }
        TaskCreationEvent event = (TaskCreationEvent) gevent;
        Task task = (Task) event.getSource();
        Set<Long> ids = task.getPerformerIds();
        for (Long id : ids) {
            List<AbstractCalendarPerformerStatistic> stats =
                    statistic.findByPerformerId(id);
            if (stats == null || stats.isEmpty()) {
                stats = initStatistics(id);
            }
            for (AbstractCalendarPerformerStatistic stat : stats) {
                process(stat);
            }
        }
    }

    private List<AbstractCalendarPerformerStatistic> initStatistics(Long perfId) {
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

    private void process(AbstractCalendarPerformerStatistic stat) {
        Boolean expiredO = stat.getExpired();
        boolean expired = expiredO == null ? true : expiredO.booleanValue();
        if (!expired) {
            increment(stat);
            statistic.update(stat);
        } else {
            statistic.delete(stat);
            AbstractCalendarPerformerStatistic newStat = (AbstractCalendarPerformerStatistic)
                    stat.clone();
            increment(newStat);
            statistic.create(newStat);
        }
    }

    private void increment(
            AbstractCalendarPerformerStatistic stat) {
        if (stat == null) {
            return;
        }
        int amount = stat.getAmount();
        amount++;
        stat.setAmount(amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof CreationTaskEventService) {
            return true;
        }
        return false;
    }
}
