package app.customtenant.events.pub;

import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.events.impl.TaskEvent;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.taskmodels.Task;
import app.tenantconfiguration.TenantContext;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("task_pub")
public class TaskEventPublisher extends GenericEventPublisher<Task> {

    public TaskEventPublisher(ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public void publish(Task entity, Performer author) {
        TaskEvent event = new TaskEvent(entity.getToDo());
        event.setTaskId(entity.getId());
        Set<Long> ids = new HashSet<>(entity.getPerformerIds());
        if (author != null) {
            event.setAuthorId(author.getId());
            ids.remove(author.getId());
        }
        event.setPerformersId(ids);
        event.setTaskId(entity.getId());
        getEventPublisher().publishEvent(new GenericDaoApplicationEvent(
                event, Event.EventType.TASK_PUB, TenantContext.getTenant())
        );
    }
}
