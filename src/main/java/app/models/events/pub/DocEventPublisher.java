package app.models.events.pub;

import app.models.basic.BriefDocument;
import app.models.basic.Performer;
import app.service.interfaces.IEventService;
import org.springframework.beans.factory.annotation.Autowired;

public class DocEventPublisher
        extends GenericEventPublisher<BriefDocument> {

    @Autowired
    public DocEventPublisher(IEventService eventService) {
        super(eventService);
    }

    @Override
    /**
     * @TODO Implement
     * @see for example app.models.events.pub.
     */
    public void publish(BriefDocument entity, Performer author) {
        throw new UnsupportedOperationException();
    }
}
