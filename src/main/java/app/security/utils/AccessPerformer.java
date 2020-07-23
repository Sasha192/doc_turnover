package app.security.utils;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class AccessPerformer extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        // @TODO : check if smbd pass to wrong performer, except MANAGER, G_MANAGER
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
