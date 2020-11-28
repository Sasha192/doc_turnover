package app.security.controllers.defaultenantfilters;

import app.configuration.spring.constants.Constants;
import app.security.wrappers.IAuthenticated;
import app.tenantconfiguration.TenantContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Simple AuthenticationFilter for filtering authenticated requests starts with "/me"
 *
 * @see AuthenticationMeFilter#doFilter(ServletRequest, ServletResponse, FilterChain)
 */
public class AuthenticationMeFilter extends GenericFilterBean {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private IAuthenticated authenticated;

    private ApplicationContext context;

    private Set<String> allowedUrls = Set
            .of("/", "/main", "/main/signup", "/main/signin", "/main/confirm");

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        this.init(getServletContext());
    }

    private void init(ServletContext context) {
        if (authenticated == null) {
            authenticated = getContext(context)
                    .getBean(IAuthenticated.class);
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
            if (isResourceRequest(uri)) {
                chain.doFilter(request, response);
                return;
            }
            boolean isAuth = isAuthenticated(req);
            HttpSession session = req.getSession(true);
            if (!isAuth) {
                if (uri.startsWith("/main/auth")) {
                    chain.doFilter(request, response);
                    return;
                } else if (uri.startsWith("/main/tenants/invite/connect")) {
                    String tenantId = req.getParameter("tenantId");
                    HttpServletResponse res = (HttpServletResponse) response;
                    Cookie cookie = new Cookie(Constants.INVITE_NO_AUTH_TENANT_SESSION_ID, tenantId);
                    cookie.setMaxAge(60 * 60);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    res.addCookie(cookie);
                    ((HttpServletResponse) response).sendRedirect("/main");
                } else if (uri.equals("/") || allowedUrls.contains(uri)) {
                    chain.doFilter(request, response);
                    return;
                }
            } else {
                Optional<Cookie> cookieTenant = Arrays.stream(req.getCookies())
                        .filter(cookie -> {
                            return cookie
                                    .getName()
                                    .equals(Constants.INVITE_NO_AUTH_TENANT_SESSION_ID);
                        }).findAny();
                if (cookieTenant.isPresent()) {
                    HttpServletResponse res = (HttpServletResponse) response;
                    Cookie cookie = cookieTenant.get();
                    String tenantId = cookie.getValue();
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    res.addCookie(cookie);
                    res.sendRedirect("/main/tenants/invite/connect/view?tenantId=" + tenantId);
                    return;
                }
                chain.doFilter(request, response);
            }
        }
    }

    private boolean isAuthenticated(HttpServletRequest req) {
        String prev = TenantContext.getTenant();
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        if (authenticated == null) {
            this.init(req.getServletContext());
        }
        boolean isAuth = authenticated.isAuthenticated(req);
        TenantContext.setTenant(prev);
        return isAuth;
    }

    private boolean isResourceRequest(String uri) {
        String[] res = Constants.RESOURCES_PATH;
        int len = res.length;
        for (int i = 0; i < len; i++) {
            if (uri.contains(res[i])) {
                return true;
            }
        }
        return false;
    }
}
