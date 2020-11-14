package app.controllers.customtenant;

import app.configuration.spring.constants.Constants;
import app.customtenant.events.Event;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IEventService;
import app.security.wrappers.IPerformerWrapper;
import app.tenantconfiguration.TenantContext;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/notifications")
public class NotificationsController extends JsonSupportController {

    private static final GsonBuilder BUILDER = Constants.BUILDER_JSON_EVENTS;

    @Autowired
    private IEventService eventService;

    @Autowired
    private IPerformerWrapper performerWrapper;

    @Autowired
    private ServerSentNotifications notifications;

    @RequestMapping(value = "/stream", method = RequestMethod.GET)
    public void register(HttpServletResponse response,
                     HttpServletRequest request)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        String notifId = ServerSentNotifications.getId(
                performer.getId(), TenantContext.getTenant()
        );
        notifications.put(notifId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("success", new JsonPrimitive(true));
        notifications.send(notifId, jsonObject,
                ServerSentNotifications.ServerEvent.INIT);
    }

    @RequestMapping(value = "/list/{page_id}", method = RequestMethod.GET)
    public void list(HttpServletResponse response,
                     HttpServletRequest request,
                     @PathVariable("page_id") Integer page)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<Event> events = eventService.retrieveForPerformer(page,
                performer.getId());
        writeToResponse(response, BUILDER, performer);
    }

    @RequestMapping(value = "/new/count", method = RequestMethod.GET)
    public void countNewEvents(HttpServletRequest request,
                               HttpServletResponse response) {
        Performer performer = performerWrapper.retrievePerformer(request);
        Long count = eventService.countNewEvents(performer.getId());
        sendDefaultJson(response, true, count.toString());
    }

    @RequestMapping(value = "/see/all", method = RequestMethod.GET)
    public void seeAll(HttpServletRequest request,
                       HttpServletResponse response) {
        Performer performer = performerWrapper.retrievePerformer(request);
        eventService.seeAllEvents(performer.getId());
        this.sendDefaultJson(response, true, "");
    }
}
