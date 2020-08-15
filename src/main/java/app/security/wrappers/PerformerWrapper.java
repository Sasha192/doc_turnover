package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.models.basic.Performer;
import app.service.interfaces.IPerformerService;
import app.service.interfaces.IPerformerUpdateEventListenerService;
import com.google.common.base.Preconditions;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class PerformerWrapper {

    private IPerformerService performerService;

    private AuthenticationWrapper authenticationWrapper;

    private final IPerformerUpdateEventListenerService listenerService;

    @Autowired
    public PerformerWrapper(IPerformerService performerService,
                            AuthenticationWrapper authenticationWrapper,
                            @Qualifier("perf_update_listener")
                                        IPerformerUpdateEventListenerService listenerService) {
        this.performerService = performerService;
        this.authenticationWrapper = authenticationWrapper;
        this.listenerService = listenerService;
    }

    public Performer retrievePerformer(HttpServletRequest request) {
        Performer performer = null;
        HttpSession session = request.getSession();
        if (session != null) {
            performer = retrievePerformer(session);
            return performer;
        }
        Principal principal = (Principal) authenticationWrapper.getPrincipal(request);
        if ((principal instanceof UserDetails)) {
            performer = performerService.retrieveByUsername(
                    ((UserDetails) principal).getUsername()
            );
            session = request.getSession(true);
            session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
        }
        Preconditions.checkNotNull(performer);
        return performer;
    }

    private Performer retrievePerformer(HttpSession session) {
        Performer performer = (Performer) session
                .getAttribute(Constants.PERFORMER_SESSION_KEY);
        Long id = performer.getId();
        if (!listenerService.toUpdate(id)) {
            return performer;
        } else {
            performer = performerService.findOne(id);
            listenerService.wasUpdated(id);
            session.setAttribute(
                    Constants.PERFORMER_SESSION_KEY, performer
            );
            return performer;
        }
    }
}
