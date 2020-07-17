package app.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class AccessGodAdminFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String requestUri = req.getRequestURI();
            SecurityContext context = SecurityContextHolder.getContext();
            if (context != null) {
                Authentication authentication = context.getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    final Collection<? extends GrantedAuthority> authorities =
                            authentication.getAuthorities();
                    if (requestUri.matches("\\/god\\/admin.*")) {
                        for (final GrantedAuthority authority : authorities) {
                            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                                filterChain.doFilter(servletRequest, servletResponse);
                                return;
                            }
                        }
                        if (servletResponse instanceof HttpServletResponse) {
                            ((HttpServletResponse) servletResponse).sendRedirect("/access/denied");
                            return;
                        }
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
