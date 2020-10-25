package app.customtenant.eventdriven.publishers;

import app.customtenant.eventdriven.domain.GenericApplicationEvent;
import app.customtenant.eventdriven.domain.TaskCreationEvent;
import app.customtenant.eventdriven.domain.TaskDeadlineEvent;
import app.customtenant.eventdriven.domain.TaskStatusModificationEvent;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
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

    public void onCreate(final Task task, String tenant) {
        GenericApplicationEvent<Task> event = new TaskCreationEvent(task, tenant);
        publisher.publishEvent(event);
    }

    public void onStatusUpdate(final Task task,
                               final TaskStatus oldStatus,
                               final TaskStatus newStatus,
                               String tenant) {
        GenericApplicationEvent<Task> event =
                new TaskStatusModificationEvent(task, oldStatus, newStatus, tenant);
        publisher.publishEvent(event);
    }

    public void onDeadlineSet(final Task task, String tenant) {
        GenericApplicationEvent<Task> event =
                new TaskDeadlineEvent(task, tenant);
        publisher.publishEvent(event);
    }
}
