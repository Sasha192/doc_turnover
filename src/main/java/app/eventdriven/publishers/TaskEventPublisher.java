package app.eventdriven.publishers;

import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskCreationEvent;
import app.eventdriven.domain.TaskDeadlineEvent;
import app.eventdriven.domain.TaskStatusModificationEvent;
import app.models.basic.TaskStatus;
import app.models.basic.taskmodels.Task;
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
        GenericApplicationEvent<Task> event = new TaskCreationEvent(task);
        publisher.publishEvent(event);
    }

    public void onStatusUpdate(final Task task,
                               final TaskStatus oldStatus,
                               final TaskStatus newStatus) {
        GenericApplicationEvent<Task> event =
                new TaskStatusModificationEvent(task, oldStatus, newStatus);
        publisher.publishEvent(event);
    }

    public void onDeadlineSet(final Task task) {
        GenericApplicationEvent<Task> event =
                new TaskDeadlineEvent(task);
        publisher.publishEvent(event);
    }
}
