package app.customtenant.events.pub;

import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.events.impl.ReportPublishingEvent;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.ITaskService;
import app.tenantconfiguration.TenantContext;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("report_pub")
public class ReportEventPublisher
        extends GenericEventPublisher<Report> {

    private final ITaskService taskService;

    public ReportEventPublisher(ApplicationEventPublisher eventPublisher,
                                ITaskService taskService) {
        super(eventPublisher);
        this.taskService = taskService;
    }

    @Override
    public void publish(Report entity, Performer author) {
        Task task = taskService.findOne(entity.getTaskId());
        ReportPublishingEvent event = new ReportPublishingEvent(task.getToDo());
        event.setReportId(entity.getId());
        Set<Long> performersIds = new HashSet<>(entity.getPerformerIds());
        if (author != null) {
            event.setAuthorId(author.getId());
            performersIds.remove(author.getId());
        }
        event.setPerformersId(performersIds);
        event.setTaskId(entity.getTaskId());
        getEventPublisher().publishEvent(new GenericDaoApplicationEvent(
                event, Event.EventType.REPORT_PUB, TenantContext.getTenant())
        );
    }
}
