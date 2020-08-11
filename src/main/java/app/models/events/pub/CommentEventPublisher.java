package app.models.events.pub;

import app.models.abstr.Comment;
import app.models.basic.Performer;
import app.models.events.impl.CommentPublishingEvent;
import app.service.interfaces.IEventService;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("comment_pub")
public class CommentEventPublisher
        extends GenericEventPublisher<Comment> {

    @Autowired
    public CommentEventPublisher(IEventService eventService) {
        super(eventService);
    }

    @Override
    public void publish(Comment entity, Performer author) {
        CommentPublishingEvent event = new CommentPublishingEvent();
        event.setCommentId(entity.getId());
        event.setAuthorId(author.getId());
        Set<Long> ids = new HashSet<>(entity.getPerformerIds());
        ids.add(author.getId());
        event.setPerformersId(ids);
        getEventService().create(event);
    }
}
