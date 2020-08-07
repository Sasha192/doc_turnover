package app.security.wrappers;

import app.models.basic.Performer;
import app.service.interfaces.IPerformerService;
import com.google.common.base.Preconditions;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    public Performer retrievePerformer(HttpServletRequest request) {
        Authentication authentication = authenticationWrapper.getAuthentication(request);
        Preconditions.checkNotNull(authentication);
        Object principal = authentication.getPrincipal();
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
