package app.security.wrappers.impl;

import app.configuration.spring.constants.Constants;
import app.security.wrappers.IAuthenticated;
import app.security.wrappers.IAuthenticationViaCookies;
import app.security.wrappers.IRequestAuthority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auth_manage_wrapper")
public class AuthenticationAccessWrapper implements
        IAuthenticated,
        IRequestAuthority {

    private IAuthenticationViaCookies authCookies;

    @Autowired
    public AuthenticationAccessWrapper(IAuthenticationViaCookies authenticatedViaCookies) {
        this.authCookies = authenticatedViaCookies;
    }

    @Override
    public boolean isAuthenticated(HttpServletRequest req) {
        Authentication auth = retrieveAuthentication(req);
        if (auth != null && auth.isAuthenticated()) {
            return true;
        } else {
            if (authCookies.authenticatedViaCookies(req) != null) {
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean hasAuthority(HttpServletRequest request,
                                SimpleGrantedAuthority authority) {
        if (isAuthenticated(request)) {
            Authentication auth = retrieveAuthentication(request);
            if (auth.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    private Authentication retrieveAuthentication(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session != null) {
            SecurityContext context = retrieveSecurityContext(session);
            if (context != null) {
                Authentication auth = context.getAuthentication();
                return auth;
            }
        }
        return null;
    }

    private SecurityContext retrieveSecurityContext(HttpSession session) {
        Object o = session.getAttribute(Constants.SPRING_SECURITY_CONTEXT_KEY);
        if (o != null && o instanceof SecurityContext) {
            return (SecurityContext) o;
        }
        return SecurityContextHolder.getContext();
    }

}
