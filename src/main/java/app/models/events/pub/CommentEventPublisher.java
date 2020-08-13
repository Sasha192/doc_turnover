package app.models.events.pub;

import app.models.abstr.TaskHolderComment;
import app.models.basic.Performer;
import app.models.events.impl.TaskCommentPublishingEvent;
import app.service.interfaces.IEventService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("comment_pub")
public class CommentEventPublisher
        extends GenericEventPublisher<TaskHolderComment> {

    @Autowired
    public CommentEventPublisher(IEventService eventService) {
        super(eventService);
    }

    @Override
    public void publish(TaskHolderComment entity, Performer author) {
        TaskCommentPublishingEvent event = new TaskCommentPublishingEvent();
        event.setCommentId(entity.getId());
        event.setAuthorId(author.getId());
        Set<Long> ids = new HashSet<>(entity.getPerformerIds());
        ids.add(author.getId());
        event.setPerformersId(ids);
        event.setTaskId(entity.getTaskId());
        getEventService().create(event);
    }
}
