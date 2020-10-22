package app.customtenant.events.pub;

import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.events.impl.TaskCommentPublishingEvent;
import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.basic.Performer;
import app.tenantconfiguration.TenantContext;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("comment_pub")
public class CommentEventPublisher
        extends GenericEventPublisher<TaskHolderComment> {

    public CommentEventPublisher(ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public void publish(TaskHolderComment entity, Performer author) {
        TaskCommentPublishingEvent event = new TaskCommentPublishingEvent();
        event.setCommentId(entity.getId());
        event.setAuthorId(author.getId());
        Set<Long> ids = new HashSet<>(entity.getPerformerIds());
        ids.remove(author.getId());
        event.setPerformersId(ids);
        event.setTaskId(entity.getTaskId());
        getEventPublisher().publishEvent(new GenericDaoApplicationEvent(
                event, Event.EventType.COMMENT_PUB, TenantContext.getTenant())
        );
    }
}
