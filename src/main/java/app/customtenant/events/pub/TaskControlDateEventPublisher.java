package app.customtenant.events.pub;

import app.configuration.spring.constants.Constants;
import app.customtenant.events.Event;
import app.customtenant.events.impl.ControlDatePublishingEvent;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.taskmodels.Task;
import app.tenantconfiguration.TenantContext;
import app.utils.CustomAppDateTimeUtil;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("task_control_date_publisher")
public class TaskControlDateEventPublisher
        extends GenericEventPublisher<Task> {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    public TaskControlDateEventPublisher(
            ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public void publish(Task entity, Performer author) {
        Date controlDate = entity.getControlDate();
        Date now = CustomAppDateTimeUtil.now();
        try {
            String message = createMessage(now, controlDate);
            ControlDatePublishingEvent event =
                    new ControlDatePublishingEvent(message, entity.getToDo());
            event.setTaskId(entity.getId());
            Set<Long> ids = new HashSet<>(entity.getPerformerIds());
            if (author != null) {
                event.setAuthorId(author.getId());
                ids.remove(author.getId());
            }
            ids.add(entity.getTaskOwnerId());
            event.setPerformersId(ids);
            event.setTaskId(entity.getId());
            getEventPublisher().publishEvent(new GenericDaoApplicationEvent(
                    event,
                    Event.EventType.CONTROL_DATE_NOTIFICATION,
                    TenantContext.getTenant())
            );
        } catch (UnsupportedOperationException ex) {
            LOGGER.error(ex);
        }
    }

    private String createMessage(Date now, Date controlDate) {
        int compareResult = now.compareTo(controlDate);
        if (compareResult < 0) {
            long nowMs = now.getTime();
            long controlMs = controlDate.getTime();
            long daysLeft = (controlMs - nowMs) / (Constants.DAY_IN_MS);
            return "Дата контролю через " + daysLeft + " днів";
        }
        if (compareResult == 0) {
            return "Сьогодні дата контролю";
        }
        throw new UnsupportedOperationException(
                "NOT SUPPORTED CONTROL DATE "
                + controlDate
                + " NOW = "
                + now);
    }
}
