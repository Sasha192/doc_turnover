package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.models.basic.Performer;
import app.service.interfaces.IPerformerService;
import com.google.common.base.Preconditions;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class PerformerWrapper {

    private IPerformerService performerService;

    private AuthenticationWrapper authenticationWrapper;

    @Autowired
    public PerformerWrapper(IPerformerService performerService,
                            AuthenticationWrapper authenticationWrapper) {
        this.performerService = performerService;
        this.authenticationWrapper = authenticationWrapper;
    }

    public Performer retrievePerformer(HttpServletRequest request) {
        Performer performer = null;
        HttpSession session = request.getSession();
        if (session != null) {
            performer = (Performer) session.getAttribute(Constants.PERFORMER_SESSION_KEY);
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
}
