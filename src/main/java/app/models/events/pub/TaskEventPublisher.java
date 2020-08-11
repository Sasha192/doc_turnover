package app.models.events.pub;

import app.models.basic.Performer;
import app.models.basic.Task;
import app.models.events.impl.TaskEvent;
import app.service.interfaces.IEventService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("task_pub")
public class TaskEventPublisher extends GenericEventPublisher<Task> {

    @Autowired
    public TaskEventPublisher(IEventService eventService) {
        super(eventService);
    }

    @Override
    public void publish(Task entity, Performer author) {
        TaskEvent event = new TaskEvent();
        event.setTaskId(entity.getId());
        event.setAuthorId(author.getId());
        Set<Long> ids = entity.getPerformerIds();
        ids.add(entity.getTaskOwnerId());
        ids.add(author.getId());
        event.setPerformersId(ids);
        getEventService().create(event);
    }
}
