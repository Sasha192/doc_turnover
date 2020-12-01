package app.customtenant.events.pub;

import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.events.impl.TaskStatusModificationEvent;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.taskmodels.Task;
import app.tenantconfiguration.TenantContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component("task_status_pub")
public class TaskStatusModificationEventPublisher
        extends GenericEventPublisher<Task> {

    public TaskStatusModificationEventPublisher(ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public void publish(Task entity, Performer author) {
        TaskStatusModificationEvent event = new TaskStatusModificationEvent(
                entity.getToDo(),
                entity.getId(),
                entity.getStatus()
        );
        Set<Long> ids = new HashSet<>(entity.getPerformerIds());
        ids.add(entity.getTaskOwnerId());
        if (author != null) {
            event.setAuthorId(author.getId());
            ids.remove(author.getId());
        }
        event.setPerformersId(ids);
        event.setTaskId(entity.getId());
        getEventPublisher().publishEvent(new GenericDaoApplicationEvent(
                event, Event.EventType.TASK_STATUS_MODIFICATION, TenantContext.getTenant())
        );
    }
}
