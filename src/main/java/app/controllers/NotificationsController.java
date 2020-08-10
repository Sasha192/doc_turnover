package app.controllers;

import app.models.basic.Performer;
import app.models.events.Event;
import app.models.events.PerformerEventAgent;
import app.security.wrappers.PerformerWrapper;
import app.service.interfaces.IEventService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/notifications")
public class NotificationsController extends JsonSupportController {

    @Autowired
    private IEventService eventService;

    @Autowired
    private PerformerWrapper performerWrapper;

    @RequestMapping(value = "recent", method = RequestMethod.GET)
    public void recentList(HttpServletResponse response,
                           HttpServletRequest request)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<PerformerEventAgent> events = eventService
                .retrieveLastEventsForPerformerId(
                        performer
                                .getId()
                );
        sendDefaultJson(response, events);
    }


}
