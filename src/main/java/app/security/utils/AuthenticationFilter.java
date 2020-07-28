package app.security.utils;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationFilter extends GenericFilterBean {

    private static final String RESOURCES_PATH_REGEX = "(\\/css.*)"
            + "|(\\/scripts.*)"
            + "|(\\/fonts.*)"
            + "|(\\/img.*)"
            + "|(\\/libs.*)"
            + "|(\\/partials.*)";

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String requestUri = req.getRequestURI();
            SecurityContext context = SecurityContextHolder.getContext();
            if (context != null) {
                Authentication authentication = context.getAuthentication();
                if (requestUri.matches(RESOURCES_PATH_REGEX)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
                if (requestUri.matches("\\/auth.*")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else if (requestUri.matches("\\/.*")) {
                    if (authentication != null && authentication.isAuthenticated()) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    } else {
                        if (servletResponse instanceof HttpServletResponse) {
                            ((HttpServletResponse) servletResponse).sendRedirect("/auth");
                            return;
                        }
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
