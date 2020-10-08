package app.customtenant.events.pub;

import app.customtenant.events.impl.ReportPublishingEvent;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.Report;
import app.customtenant.service.interfaces.IEventService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("report_pub")
public class ReportEventPublisher
        extends GenericEventPublisher<Report> {

    @Autowired
    public ReportEventPublisher(IEventService eventService) {
        super(eventService);
    }

    @Override
    public void publish(Report entity, Performer author) {
        ReportPublishingEvent event = new ReportPublishingEvent();
        event.setReportId(entity.getId());
        event.setAuthorId(author.getId());
        Set<Long> performersIds = new HashSet<>(entity.getTask().getPerformerIds());
        Long taskOwnerId = entity
                .getTask()
                .getTaskOwnerId();
        if (!taskOwnerId.equals(author.getId())) {
            performersIds.add(entity.getTask().getTaskOwnerId());
        }
        performersIds.remove(author.getId());
        event.setPerformersId(performersIds);
        event.setTaskId(entity.getTask().getId());
        getEventService().create(event);
    }
}
