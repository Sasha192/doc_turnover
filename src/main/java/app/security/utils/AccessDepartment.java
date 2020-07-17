package app.security.utils;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class AccessDepartment extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        // @TODO : check if smbd pass to another depo, except G_MANAGER
        // @TODO : secretary only download docs
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
