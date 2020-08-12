package app.security.wrappers;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.google.common.base.Preconditions;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Component
public class AuthenticationWrapper {

    public SecurityContext getSecurityContext(HttpServletRequest req) {
        SecurityContext sc = null;
        HttpSession session = req.getSession();
        if (session != null) {
            Object o = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            if (null != o && o instanceof SecurityContext) {
                sc = (SecurityContext) o;
            }
        }
        if (sc == null) {
            sc = SecurityContextHolder.getContext();
        }
        return sc;
    }

    public Object getPrincipal(HttpServletRequest request) {
        Authentication authentication = getAuthentication(request);
        Preconditions.checkNotNull(authentication);
        return authentication.getPrincipal();
    }

    public Authentication getAuthentication(HttpServletRequest req) {
        SecurityContext sc = getSecurityContext(req);
        Preconditions.checkNotNull(sc);
        return getAuthentication(sc, req);
    }

    private Authentication getAuthentication(SecurityContext sc,
                                             HttpServletRequest request) {
        Authentication auth = sc.getAuthentication();
        if (auth == null) {
                HttpSession session = request.getSession(true);
                sc.setAuthentication(auth);
                session.setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        sc);
            return auth;
        } else {
            return auth;
        }
    }
}
