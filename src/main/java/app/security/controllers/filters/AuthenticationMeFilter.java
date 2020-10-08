package app.security.controllers.filters;

import app.configuration.spring.constants.Constants;
import app.security.wrappers.IAuthenticated;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Simple AuthenticationFilter for filtering authenticated requests starts with "/me"
 * @see AuthenticationMeFilter#doFilter(ServletRequest, ServletResponse, FilterChain)
 *
 */
public class AuthenticationMeFilter extends GenericFilterBean {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private IAuthenticated authenticated;

    private ApplicationContext context;

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
            if (uri.startsWith("/")) {
                if (uri.startsWith("/me")) {
                    if (isAuth) {
                        chain.doFilter(request, response);
                        return;
                    }
                    ((HttpServletResponse) response).sendRedirect("/");
                    return;
                }
                chain.doFilter(request, response);
                return;
            }
        }
    }

    private boolean isAuthenticated(HttpServletRequest req) {
        if (authenticated == null) {
            this.init(req.getServletContext());
        }
        return authenticated.isAuthenticated(req);
    }

    private boolean isResourceRequest(String uri) {
        String[] res = Constants.RESOURCES_PATH;
        int len = res.length;
        for (int i = 0; i < len; i++) {
            if (uri.startsWith(res[i])) {
                return true;
            }
        }
        return false;
    }
}
