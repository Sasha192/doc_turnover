package app.security.controllers.defaultenantfilters;

import app.configuration.spring.constants.Constants;
import app.tenantconfiguration.TenantContext;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.filter.GenericFilterBean;

public class TenantRequiredFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpSession session = req.getSession(true);
            Object tenant = session.getAttribute(Constants.TENANT_SESSION_ID);
            if (tenant == null) {
                TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
                session.setAttribute(Constants.TENANT_SESSION_ID,
                        TenantContext.DEFAULT_TENANT_IDENTIFIER);
            } else {
                TenantContext.setTenant((String) tenant);
            }
        }
    }
}
