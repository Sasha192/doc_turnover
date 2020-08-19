package app.security.wrappers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public interface IAuthenticationWrapper {

    public SecurityContext getSecurityContext(HttpServletRequest req);

    public Object getPrincipal(HttpServletRequest request);

    public Authentication getAuthentication(HttpServletRequest req);

}
