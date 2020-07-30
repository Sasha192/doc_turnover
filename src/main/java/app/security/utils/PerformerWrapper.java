package app.security.utils;

import app.models.Performer;
import app.security.models.AuthenticationWrapper;
import app.service.IPerformerService;
import com.google.common.base.Preconditions;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

    /**
     * Used for assigning default performer, user;
     *
     * @deprecated use {@link #retrievePerformer(HttpServletRequest)} instead.
     */
    @Deprecated
    public Performer retrievePerformer() {
        try {
            Principal principal = (Principal) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            Performer performer = null;
            if ((principal instanceof UserDetails)) {
                performer = performerService.retrieveByUsername(
                        ((UserDetails) principal).getUsername()
                );
            }
            Preconditions.checkNotNull(performer);
            return performer;
        } catch (Exception e) {
            return performerService.findOne(1);
        }
    }
    // @TODO : change default performer!

    public Performer retrievePerformer(HttpServletRequest request) {
        Authentication authentication = authenticationWrapper.getAuthentication(request);
        Preconditions.checkNotNull(authentication);
        Principal principal = (Principal) authentication.getPrincipal();
        Performer performer = null;
        if ((principal instanceof UserDetails)) {
            performer = performerService.retrieveByUsername(
                    ((UserDetails) principal).getUsername()
            );
        }
        Preconditions.checkNotNull(performer);
        return performer;
    }

}
