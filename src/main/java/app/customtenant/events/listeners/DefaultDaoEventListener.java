package app.customtenant.events.listeners;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.ServerSentNotifications;
import app.customtenant.events.Event;
import app.customtenant.events.impl.GenericDaoApplicationEvent;
import app.customtenant.service.interfaces.IEventService;
import app.tenantconfiguration.TenantContext;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DefaultDaoEventListener
        implements ApplicationListener<GenericDaoApplicationEvent> {

    private static final GsonBuilder BUILDER = Constants.BUILDER_JSON_EVENTS;

    private final IEventService eventService;

    private final ServerSentNotifications sentNotifications;

    @Autowired
    public DefaultDaoEventListener(IEventService eventService,
                                   ServerSentNotifications sentNotifications,
                                   ApplicationEventPublisher publisher) {
        this.eventService = eventService;
        this.sentNotifications = sentNotifications;
    }

    @Override
    @Async
    public void onApplicationEvent(GenericDaoApplicationEvent genEvent) {
        String prevTenant = TenantContext.getTenant();
        Event event = genEvent.getSource();
        String tenant = event.getTenantId();
        TenantContext.setTenant(tenant);
        eventService.create(event);
        Set<Long> performerIds = event.getPerformersId();
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
        } finally {
            TenantContext.setTenant(prevTenant);
        }
    }
}
