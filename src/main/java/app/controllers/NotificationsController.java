package app.controllers;

import app.configuration.spring.constants.Constants;
import app.events.PerformerEventAgent;
import app.models.basic.Performer;
import app.models.serialization.ExcludeStrategies;
import app.security.wrappers.IPerformerWrapper;
import app.service.interfaces.IEventService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/notifications")
public class NotificationsController extends JsonSupportController {

    private static final GsonBuilder BUILDER;

    static {
        BUILDER = new GsonBuilder()
                .setExclusionStrategies(ExcludeStrategies.EXCLUDE_FOR_JSON_EVENT)
                .setDateFormat(Constants.DATE_FORMAT.toPattern())
                .setPrettyPrinting();
    }

    @Autowired
    private IEventService eventService;

    @Autowired
    private IPerformerWrapper performerWrapper;

    @RequestMapping(value = "/recent", method = RequestMethod.GET)
    public void recentList(HttpServletResponse response,
                           HttpServletRequest request)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<PerformerEventAgent> events = eventService
                .retrieveLastEventsForPerformerId(performer.getId());
        writeToResponse(response, BUILDER, events);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response,
                           HttpServletRequest request)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<PerformerEventAgent> events = eventService
                .retrieveLastEventsForPerformerId(performer.getId());
        writeToResponse(response, BUILDER, events);
    }

    @RequestMapping(value = "/see", method = RequestMethod.POST)
    public void seeEvent(HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam("event_id") Long eventId) {
        Performer performer = performerWrapper.retrievePerformer(request);
        eventService.seeEvent(eventId, performer.getId());
        sendDefaultJson(response, true, "");
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
