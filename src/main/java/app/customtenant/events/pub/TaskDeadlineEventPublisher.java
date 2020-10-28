package app.customtenant.events.pub;

import app.configuration.spring.constants.Constants;
import app.customtenant.events.Event;
import app.customtenant.events.impl.DeadlinePublishingEvent;
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

@Component("task_deadline_date_publisher")
public class TaskDeadlineEventPublisher
        extends GenericEventPublisher<Task> {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    public TaskDeadlineEventPublisher(
            ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public void publish(Task entity, Performer author) {
        Date deadlineDate = entity.getDeadlineDate();
        Date now = CustomAppDateTimeUtil.now();
        try {
            String message = createMessage(now, deadlineDate);
            DeadlinePublishingEvent event = new DeadlinePublishingEvent(message);
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
                    Event.EventType.DEADLINE_NOTIFICATION,
                    TenantContext.getTenant())
            );
        } catch (UnsupportedOperationException ex) {
            LOGGER.error(ex);
        }
    }

    private String createMessage(Date now, Date deadLine) {
        int compareResult = now.compareTo(deadLine);
        if (compareResult < 0) {
            long nowMs = now.getTime();
            long controlMs = deadLine.getTime();
            long daysLeft = (controlMs - nowMs) / (Constants.DAY_IN_MS);
            return "Дата дедлайну через " + daysLeft + " днів";
        } else if (compareResult == 0) {
            return "Сьогодні дата дедлайну";
        } else {
            return "Просрочено дедлайн : " + deadLine;
        }
    }
}
