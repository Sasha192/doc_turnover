package app.customtenant.eventdriven.service;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;
import app.customtenant.eventdriven.domain.TaskEventEnum;
import app.customtenant.eventdriven.domain.TaskStatusModificationEvent;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.service.ClonePerformerStatisticComponent;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskStatusModificationEventService
        implements ITaskEventService<TaskStatusModificationEvent> {

    @Autowired
    private ICalendarStatistic statistic;

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.STATUS_MODIFICATION;
    }

    @Override
    @Transactional
    public void service(GenericApplicationEvent gevent) {
        TaskStatusModificationEvent event = (TaskStatusModificationEvent) gevent;
        TaskStatus.DefaultStatus oldS = event.getOldStatus().getStatus();
        TaskStatus.DefaultStatus newS = event.getNewStatus().getStatus();
        Function<AbstractCalendarPerformerStatistic,
                AbstractCalendarPerformerStatistic> operator =
                oldS.decrement().andThen(newS.increment());
        Task task = (Task) event.getSource();
        Set<Long> ids = task.getPerformerIds();
        for (Long id : ids) {
            List<AbstractCalendarPerformerStatistic> stats =
                    statistic.findByPerformerId(id);
            for (AbstractCalendarPerformerStatistic stat : stats) {
                process(stat, operator);
            }
        }

    }

    private void process(AbstractCalendarPerformerStatistic stat,
                         Function<
                                 AbstractCalendarPerformerStatistic,
                                 AbstractCalendarPerformerStatistic>
                                 operator) {
        Boolean expiredO = stat.getExpired();
        boolean expired = expiredO == null ? true : expiredO.booleanValue();
        if (!expired) {
            operator.apply(stat);
            statistic.update(stat);
        } else {
            statistic.delete(stat);
            AbstractCalendarPerformerStatistic newStat;
            newStat = (AbstractCalendarPerformerStatistic)
                    ClonePerformerStatisticComponent.clone(stat);
            operator.apply(newStat);
            statistic.create(newStat);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof TaskStatusModificationEventService) {
            return true;
        }
        return false;
    }
}
