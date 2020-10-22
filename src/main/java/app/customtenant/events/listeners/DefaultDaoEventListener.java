package app.customtenant.events.listeners;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.ServerSentNotifications;
import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.service.interfaces.IEventService;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultDaoEventListener
        implements ApplicationListener<GenericDaoApplicationEvent> {

    private static final GsonBuilder BUILDER = Constants.BUILDER_JSON_EVENTS;

    private final IEventService eventService;

    private final ServerSentNotifications sentNotifications;

    private final ApplicationEventPublisher publisher;

    @Autowired
    public DefaultDaoEventListener(IEventService eventService,
                                   ServerSentNotifications sentNotifications,
                                   ApplicationEventPublisher publisher) {
        this.eventService = eventService;
        this.sentNotifications = sentNotifications;
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(GenericDaoApplicationEvent genEvent) {
        Event event = genEvent.getSource();
        eventService.create(event);
        Set<Long> performerIds = event.getPerformersId();
        String tenant = genEvent.getTenant();
        JsonElement element = BUILDER.create().toJsonTree(event);
        try {
            for (Long id : performerIds) {
                String sendNotifID = ServerSentNotifications.getId(id, tenant);
                if (sentNotifications.contains(sendNotifID)) {
                    sentNotifications.send(sendNotifID, element,
                            ServerSentNotifications.ServerEvent.NOTIFICATION);
                }
            }
        } catch (IOException e) {
            ;
        }
    }
}
