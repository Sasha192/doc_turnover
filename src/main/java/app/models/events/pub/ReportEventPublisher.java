package app.models.events.pub;

import app.models.basic.Performer;
import app.models.basic.Report;
import app.models.events.impl.ReportPublishingEvent;
import app.service.interfaces.IEventService;
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
        performersIds.add(entity.getTask().getTaskOwnerId());
        performersIds.add(author.getId());
        event.setPerformersId(performersIds);
        getEventService().create(event);
    }
}
