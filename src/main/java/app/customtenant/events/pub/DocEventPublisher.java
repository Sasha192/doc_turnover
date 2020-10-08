package app.customtenant.events.pub;

import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IEventService;
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
     * @see for example app.customtenant.events.pub.
     */
    public void publish(BriefDocument entity, Performer author) {
        throw new UnsupportedOperationException();
    }
}
