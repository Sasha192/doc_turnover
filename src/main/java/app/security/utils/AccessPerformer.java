package app.security.utils;

import app.security.models.AuthenticationWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.GenericFilterBean;

public class AccessPerformer extends GenericFilterBean {

    @Autowired
    private AuthenticationWrapper authenticationWrapper;

    public AccessPerformer(AuthenticationWrapper authenticationWrapper) {
        this.authenticationWrapper = authenticationWrapper;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        // @TODO : check if smbd pass to wrong performer, except MANAGER, G_MANAGER
        // @TODO : add filter for no HttpServlerRequest!!!
        if (servletRequest instanceof HttpServletRequest) {
            Authentication auth = authenticationWrapper
                    .getAuthentication((HttpServletRequest) servletRequest);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
