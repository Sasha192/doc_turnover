package app.customtenant.eventdriven.service;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;
import app.customtenant.eventdriven.domain.TaskDeadlineEvent;
import app.customtenant.eventdriven.domain.TaskEventEnum;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.service.ClonePerformerStatisticComponent;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeadlineTaskApplicationEventService
        implements ITaskApplicationEventService<TaskDeadlineEvent> {

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
        boolean expired = stat.getEnd().before(stat.getStart());
        if (!expired) {
            stat.incrementExpiredDeadline();
            statistic.update(stat);
        } else {
            statistic.delete(stat);
            AbstractCalendarPerformerStatistic newStat;
            newStat = (AbstractCalendarPerformerStatistic)
                    ClonePerformerStatisticComponent.clone(stat);
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
        if (obj instanceof DeadlineTaskApplicationEventService) {
            return true;
        }
        return false;
    }
}
