package app.security.controllers.defaultenantfilters;

import app.configuration.spring.constants.Constants;
import app.security.models.auth.CustomUser;
import app.security.wrappers.ICustomUserWrapper;
import app.tenantconfiguration.TenantContext;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

public class TenantRequiredFilter extends GenericFilterBean {

    private ICustomUserWrapper userWrapper;

    private ApplicationContext context;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        this.init(getServletContext());
    }

    private void init(ServletContext context) {
        if (userWrapper == null) {
            userWrapper = getContext(context)
                    .getBean(ICustomUserWrapper.class);
        }
    }

    private ApplicationContext getContext(ServletContext servletContext) {
        if (context == null) {
            this.context = WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
        }
        return context;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpSession session = req.getSession(true);
            Object tenant = session.getAttribute(Constants.TENANT_SESSION_ID);
            String uri = req.getRequestURI();
            if (uri.startsWith("/main")) {
                TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
            } else if (uri.startsWith("/com")) {
                CustomUser user = userWrapper.retrieveUser(req);
                if (user == null) {
                    ((HttpServletResponse) response).sendRedirect("/");
                    return;
                }
                String activeTenant = user.getUserInfo().getActiveTenant();
                if (activeTenant == null) {
                    activeTenant = TenantContext.PHANTOM_TENANT_IDENTIFIER;
                }
                TenantContext.setTenant(activeTenant);
                session.setAttribute(Constants.TENANT_SESSION_ID, activeTenant);
            }
        }
        chain.doFilter(request, response);
    }
}
