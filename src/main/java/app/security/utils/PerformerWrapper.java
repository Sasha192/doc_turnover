package app.security.utils;

import app.models.Performer;
import app.service.IPerformerService;
import com.google.common.base.Preconditions;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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

    public Performer retrievePerformer(HttpServletRequest  request) {
        SecurityContext sc = null;
        HttpSession session = request.getSession();
        if (session != null) {
            Object o = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            if (o != null && o instanceof SecurityContext) {
                sc = (SecurityContext) o;
            }
        } else {
            sc = SecurityContextHolder
                    .getContext();
        }
        Authentication authentication = sc.getAuthentication();
        Preconditions.checkNotNull(authentication);
        Principal principal = (Principal) sc.getAuthentication().getPrincipal();
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
