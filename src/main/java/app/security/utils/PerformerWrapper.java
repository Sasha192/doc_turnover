package app.security.utils;

import app.models.Performer;
import app.service.IPerformerService;
import com.google.common.base.Preconditions;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class PerformerWrapper {

    @Autowired
    private IPerformerService performerService;

    public Performer retrievePerformer() {
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
    }

}
