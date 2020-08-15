package app.security.controllers.filters;

import app.configuration.spring.constants.Constants;
import app.security.wrappers.AuthenticationWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationFilter extends GenericFilterBean {

    /*private static final String REG_EX = "\\/[a-zA-Z]+(\\?([a-zA-Z]=.*)+)*";
    private static final String NEG_REG_EX = "(?!" + REG_EX + "$).*";*/

    private AuthenticationWrapper authWrapper;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        // @TODO : if servlerRequest is not HttpServletRequest ? session is NULL
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            if (authWrapper == null) {
                authWrapperInitialization(req);
            }
            String requestUri = req.getRequestURI();
            if (isResourceRequest(requestUri)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            HttpSession session = req.getSession();
            if (session != null) {
                SecurityContext context = retrieveSecurityContext(session);
                if (context != null) {
                    Authentication authentication = context.getAuthentication();
                    if (requestUri.startsWith("/auth")) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    } else if (requestUri.equals(Constants.EMPTY_STRING)
                            || requestUri.startsWith("/")) {
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

    private void authWrapperInitialization(HttpServletRequest req) {
        ServletContext context = req.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getWebApplicationContext(context);
        authWrapper = webApplicationContext.getBean(AuthenticationWrapper.class);
    }

    private boolean isResourceRequest(String uri) {
        String[] res = Constants.RESOURCES_PATH;
        int len = res.length;
        for (int i = 0; i < len; i++) {
            if (uri.startsWith(res[i])) {
                return true;
            }
        }
        return false;
    }

    private SecurityContext retrieveSecurityContext(HttpSession session) {
        Object o = session.getAttribute(Constants.SPRING_SECURITY_CONTEXT_KEY);
        if (o != null && o instanceof SecurityContext) {
            return (SecurityContext) o;
        }
        return SecurityContextHolder.getContext();
    }
}
