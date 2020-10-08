package app.security.controllers.filters;

import app.security.service.IGpanelCheckPermission;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

public class GpanelAccessFilter extends GenericFilterBean {

    private IGpanelCheckPermission checkPermissions;

    private ApplicationContext context;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
    }

    private void init(ServletContext context) {
        if (checkPermissions == null) {
            checkPermissions = getContext(context)
                    .getBean(IGpanelCheckPermission.class);
        }
    }

    private ApplicationContext getContext(ServletContext servletContext) {
        if (context == null) {
            WebApplicationContext webApplicationContext = WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
            this.context = webApplicationContext;
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
            String uri = req.getRequestURI();
            if (uri.startsWith("/gpanel")) {
                if (checkPermissions.check(req)) {
                    chain.doFilter(request, response);
                    return;
                }
            } else {
                chain.doFilter(request, response);
                return;
            }
            RestrictRedirectUtil.restrict(response);
        }
    }
}
