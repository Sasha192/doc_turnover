package app.eventdriven.service;

import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskCreationEvent;
import app.eventdriven.domain.TaskEventEnum;
import app.models.basic.Task;
import app.service.interfaces.ICalendarStatistic;
import app.service.interfaces.IPerformerStatisticCreation;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
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

    @Autowired
    private IPerformerStatisticCreation statisticCreation;

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
                stats = statisticCreation.create(id);
            }
            for (AbstractCalendarPerformerStatistic stat : stats) {
                process(stat);
            }
        }
    }

    private void process(AbstractCalendarPerformerStatistic stat) {
        Boolean expiredO = stat.getExpired();
        boolean expired = expiredO == null ? true : expiredO.booleanValue();
        if (!expired) {
            stat.incrementAmount();
            stat.incrementNew();
            statistic.update(stat);
        } else {
            statistic.delete(stat);
            AbstractCalendarPerformerStatistic newStat = (AbstractCalendarPerformerStatistic)
                    stat.clone();
            newStat.incrementAmount();
            newStat.incrementNew();
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
        if (obj instanceof CreationTaskEventService) {
            return true;
        }
        return false;
    }
}
