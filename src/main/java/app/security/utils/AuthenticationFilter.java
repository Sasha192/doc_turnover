package app.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String requestUri = req.getRequestURI();
            Authentication authentication = SecurityContextHolder
                    .getContext()
                    .getAuthentication();
            if (requestUri.matches("\\/auth.*")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else if (requestUri.matches("\\/.*")) {
                if (authentication != null && authentication.isAuthenticated()) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    if (servletResponse instanceof HttpServletResponse) {
                        ((HttpServletResponse) servletResponse).sendRedirect("/auth");
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
