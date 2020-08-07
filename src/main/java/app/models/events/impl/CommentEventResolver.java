package app.models.events.impl;

import app.models.events.Event;
import app.models.events.EventPerformerResolver;
import java.util.LinkedList;
import java.util.List;

public class CommentEventResolver implements EventPerformerResolver {
    @Override
    public void resolve(Event event) {
        if (!(event instanceof CommentPublishingEvent)) {
            return;
        }
        CommentPublishingEvent comEvent = (CommentPublishingEvent) event;
        List<Long> performersId = new LinkedList<>();
        performersId.add(comEvent.getPublisher().getId());
        //////////////////////////////////////////////////////
    }
}
