package app.security.controllers.defaultenantfilters;

import app.configuration.spring.constants.Constants;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.filter.GenericFilterBean;

public class SessionRequiredFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpSession session = req.getSession();
            if (session == null) {
                session = req.getSession(true);
            }
            session.setMaxInactiveInterval(Constants.MAX_INACTIVE_SESSION_INTERVAL_SECONDS);
            chain.doFilter(req, response);
        }
    }
}
