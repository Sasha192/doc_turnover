package app.events.pub;

import app.events.impl.TaskEvent;
import app.models.basic.Performer;
import app.models.basic.taskmodels.Task;
import app.service.interfaces.IEventService;
import java.util.HashSet;
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
        Set<Long> ids = new HashSet<>(entity.getPerformerIds());
        ids.remove(author.getId());
        event.setPerformersId(ids);
        event.setTaskId(entity.getId());
        getEventService().create(event);
    }
}
