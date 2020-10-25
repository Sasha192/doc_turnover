package app.customtenant.eventdriven.service;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;
import app.customtenant.eventdriven.domain.TaskCreationEvent;
import app.customtenant.eventdriven.domain.TaskEventEnum;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.service.interfaces.IPerformerStatisticCreation;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.service.ClonePerformerStatisticComponent;
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
        boolean expired = stat.getEnd().before(stat.getStart());
        if (!expired) {
            stat.incrementAmount();
            stat.incrementNew();
            statistic.update(stat);
        } else {
            statistic.delete(stat);
            AbstractCalendarPerformerStatistic newStat;
            newStat = (AbstractCalendarPerformerStatistic)
            ClonePerformerStatisticComponent.clone(stat);
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
