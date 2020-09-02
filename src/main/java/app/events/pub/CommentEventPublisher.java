package app.events.pub;

import app.events.impl.TaskCommentPublishingEvent;
import app.models.abstr.TaskHolderComment;
import app.models.basic.Performer;
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
        ids.remove(author.getId());
        /**
         *  @see TaskHolderComment#getPerformerIds()  -> returns performersIds
         *  relative to task for TaskHolderComment
         */
        event.setPerformersId(ids);
        event.setTaskId(entity.getTaskId());
        getEventService().create(event);
    }
}
