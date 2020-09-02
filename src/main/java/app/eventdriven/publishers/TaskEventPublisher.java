package app.eventdriven.publishers;

import app.eventdriven.domain.CreationTaskEvent;
import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskStatusModificationEvent;
import app.models.basic.Task;
import app.models.basic.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TaskEventPublisher {

    private ApplicationEventPublisher publisher;

    @Autowired
    public TaskEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void onCreate(final Task task) {
        GenericApplicationEvent<Task> event = new CreationTaskEvent(task);
        publisher.publishEvent(event);
    }

    public void onStatusUpdate(final Task task,
                               final TaskStatus oldStatus,
                               final TaskStatus newStatus) {
        GenericApplicationEvent<Task> event =
                new TaskStatusModificationEvent(task, oldStatus, newStatus);
        publisher.publishEvent(event);
    }
}
