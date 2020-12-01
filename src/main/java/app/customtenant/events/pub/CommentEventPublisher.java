package app.customtenant.events.pub;

import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.events.impl.TaskCommentPublishingEvent;
import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.ITaskService;
import app.tenantconfiguration.TenantContext;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("comment_pub")
public class CommentEventPublisher
        extends GenericEventPublisher<TaskHolderComment> {

    private final ITaskService taskService;

    public CommentEventPublisher(ApplicationEventPublisher eventPublisher,
                                 ITaskService taskService) {
        super(eventPublisher);
        this.taskService = taskService;
    }

    @Override
    public void publish(TaskHolderComment entity, Performer author) {
        Task task = taskService.findOne(entity.getTaskId());
        TaskCommentPublishingEvent event =
                new TaskCommentPublishingEvent(task.getToDo());
        event.setCommentId(entity.getId());
        Set<Long> ids = new HashSet<>(entity.getPerformerIds());
        if (author != null) {
            event.setAuthorId(author.getId());
            ids.remove(author.getId());
        }
        event.setPerformersId(ids);
        event.setTaskId(entity.getTaskId());
        getEventPublisher().publishEvent(new GenericDaoApplicationEvent(
                event, Event.EventType.COMMENT_PUB, TenantContext.getTenant())
        );
    }
}
