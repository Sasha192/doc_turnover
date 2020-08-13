package app.security.controllers.filters;

import app.configuration.spring.constants.Constants;
import app.security.wrappers.AuthenticationWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationFilter extends GenericFilterBean {

    private static final String REG_EX = "\\/[a-zA-Z]+(\\?([a-zA-Z]=.*)+)*";
    private static final String NEG_REG_EX = "(?!" + REG_EX + "$).*";

    private AuthenticationWrapper authWrapper;

    public AuthenticationFilter(AuthenticationWrapper authWrapper) {
        this.authWrapper = authWrapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        // @TODO : if servlerRequest is not HttpServletRequest ? session is NULL
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String requestUri = req.getRequestURI();
            HttpSession session = req.getSession();
            if (session != null) {
                SecurityContext context = retrieveSecurityContext(session);
                if (context != null) {
                    Authentication authentication = context.getAuthentication();
                    if (requestUri.matches(Constants.RESOURCES_PATH_REGEX)) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                    if (requestUri.matches("\\/auth.*")) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    } else if (requestUri.equals(Constants.EMPTY_STRING)
                            || requestUri.matches("\\/.*")) {
                        if (authentication != null && authentication.isAuthenticated()) {
                            if (requestUri.equals(Constants.EMPTY_STRING)) {
                                ((HttpServletResponse) servletResponse).sendRedirect("/myboard");
                            }
                            filterChain.doFilter(servletRequest, servletResponse);
                            return;
                        } else {
                            if (servletResponse instanceof HttpServletResponse) {
                                ((HttpServletResponse) servletResponse).sendRedirect("/auth");
                                return;
                            }
                        }
                    }
                } else {
                    // @TODO : what if context == null ???
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private SecurityContext retrieveSecurityContext(HttpSession session) {
        Object o = session.getAttribute(Constants.SPRING_SECURITY_CONTEXT_KEY);
        if (o != null && o instanceof SecurityContext) {
            return (SecurityContext) o;
        }
        return SecurityContextHolder.getContext();
    }
}
