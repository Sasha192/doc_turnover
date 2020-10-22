package app.customtenant.events.pub;

import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.events.impl.ReportPublishingEvent;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.Report;
import app.tenantconfiguration.TenantContext;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("report_pub")
public class ReportEventPublisher
        extends GenericEventPublisher<Report> {

    public ReportEventPublisher(ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public void publish(Report entity, Performer author) {
        ReportPublishingEvent event = new ReportPublishingEvent();
        event.setReportId(entity.getId());
        event.setAuthorId(author.getId());
        Set<Long> performersIds = new HashSet<>(entity.getPerformerIds());
        performersIds.remove(author.getId());
        event.setPerformersId(performersIds);
        event.setTaskId(entity.getTaskId());
        getEventPublisher().publishEvent(new GenericDaoApplicationEvent(
                event, Event.EventType.REPORT_PUB, TenantContext.getTenant())
        );
    }
}
