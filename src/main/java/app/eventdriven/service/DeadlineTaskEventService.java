package app.eventdriven.service;

import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskDeadlineEvent;
import app.eventdriven.domain.TaskEventEnum;
import app.models.basic.Task;
import app.service.interfaces.ICalendarStatistic;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeadlineTaskEventService
        implements ITaskEventService<TaskDeadlineEvent> {

    @Autowired
    private ICalendarStatistic statistic;

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.DEADLINE_EXPIRED;
    }

    @Override
    public void service(GenericApplicationEvent gevent) {
        if (!(gevent instanceof TaskDeadlineEvent)) {
            return;
        }
        TaskDeadlineEvent event = (TaskDeadlineEvent) gevent;
        Task task = (Task) event.getSource();
        Set<Long> ids = task.getPerformerIds();
        for (Long id : ids) {
            List<AbstractCalendarPerformerStatistic> stats =
                    statistic.findByPerformerId(id);
            for (AbstractCalendarPerformerStatistic stat : stats) {
                process(stat);
            }
        }
    }

    private void process(
            AbstractCalendarPerformerStatistic stat) {
        Boolean expiredO = stat.getExpired();
        boolean expired = expiredO == null ? true : expiredO.booleanValue();
        if (!expired) {
            stat.incrementExpiredDeadline();
            statistic.update(stat);
        } else {
            statistic.delete(stat);
            AbstractCalendarPerformerStatistic newStat = (AbstractCalendarPerformerStatistic)
                    stat.clone();
            newStat.incrementExpiredDeadline();
            statistic.create(newStat);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof DeadlineTaskEventService) {
            return true;
        }
        return false;
    }
}
