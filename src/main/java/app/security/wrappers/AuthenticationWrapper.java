package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationWrapper {

    public SecurityContext getSecurityContext(HttpServletRequest req) {
        SecurityContext sc = null;
        HttpSession session = req.getSession();
        if (session != null) {
            Object o = session.getAttribute(Constants.SPRING_SECURITY_CONTEXT_KEY);
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
        if (!(authentication == null)) {
            return authentication.getPrincipal();
        }
        return null;
    }

    public Authentication getAuthentication(HttpServletRequest req) {
        SecurityContext sc = getSecurityContext(req);
        if (!(sc == null)) {
            return sc.getAuthentication();
        }
        return null;
    }

    /*private Authentication getAuthentication(SecurityContext sc,
                                             HttpServletRequest request) {
        Authentication auth = sc.getAuthentication();
        if (auth == null) {
            try {
                auth = auth(request);
            } catch (Exception e) {
                System.out.println();
            }
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

    @Deprecated
    private Authentication auth(HttpServletRequest request) {
        // :TODO REMOVE THIS!!!
        HttpSession session = request.getSession();
        session.invalidate();
        session = request.getSession(true);
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(
                "sasha192.bunin@gmail.com", "sasha192.bunin@gmail.com");
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                sc);
        return auth;
    }*/
}
