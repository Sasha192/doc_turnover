package app.security.controllers.filters;

import app.configuration.spring.constants.Constants;
import app.models.basic.CustomUser;
import app.security.models.DefaultUserDetails;
import app.security.models.RememberMeToken;
import app.security.service.IUserService;
import app.security.utils.RememberMeUtil;
import app.security.wrappers.IAuthenticationManagement;
import app.security.wrappers.IAuthenticationWrapper;
import java.io.IOException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationFilter extends GenericFilterBean {

    /*private static final String REG_EX = "\\/[a-zA-Z]+(\\?([a-zA-Z]=.*)+)*";
    private static final String NEG_REG_EX = "(?!" + REG_EX + "$).*";*/

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private IAuthenticationWrapper authWrapper;

    private IAuthenticationManagement authManager;

    private IUserService userService;

    private ApplicationContext context;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        init(getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        // @TODO : if servlerRequest is not HttpServletRequest ? session is NULL
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String requestUri = req.getRequestURI();
            if (isResourceRequest(requestUri)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            HttpSession session = req.getSession();
            if (session != null) {
                SecurityContext context = retrieveSecurityContext(session);
                if (context != null) {
                    Authentication authentication = context.getAuthentication();
                    if (authentication != null
                            && authentication.isAuthenticated()) {
                        if (requestUri.startsWith("/auth")) {
                            if (requestUri.equals("/auth")) {
                                ((HttpServletResponse) servletResponse).sendRedirect("/myboard");
                                return;
                            } else {
                                filterChain.doFilter(servletRequest, servletResponse);
                                return;
                            }
                        }
                    }
                    if (requestUri.startsWith("/auth")) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                    if (requestUri.equals(Constants.EMPTY_STRING)
                            || requestUri.startsWith("/")) {
                        if (authentication != null && authentication.isAuthenticated()) {
                            if (requestUri.equals(Constants.EMPTY_STRING)
                                    || requestUri.equals("/")) {
                                ((HttpServletResponse) servletResponse).sendRedirect("/myboard");
                                return;
                            }
                            filterChain.doFilter(servletRequest, servletResponse);
                            return;
                        } else {
                            if (authenticatedViaCookies(req)) {
                                if (requestUri.equals(Constants.EMPTY_STRING)
                                        || requestUri.equals("/")) {
                                    ((HttpServletResponse) servletResponse)
                                            .sendRedirect("/myboard");
                                    return;
                                }
                                filterChain.doFilter(servletRequest, servletResponse);
                                return;
                            } else {
                                if (servletResponse instanceof HttpServletResponse) {
                                    ((HttpServletResponse) servletResponse).sendRedirect("/auth");
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    // @TODO : what if context == null ???
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void init(ServletContext context) {
        if (authWrapper == null) {
            authWrapper = getContext(context)
                    .getBean(IAuthenticationWrapper.class);
        }
        if (userService == null) {
            userService = getContext(context)
                    .getBean(IUserService.class);
        }
        if (authManager == null) {
            authManager = getContext(context)
                    .getBean(IAuthenticationManagement.class);
        }
    }

    private boolean authenticatedViaCookies(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return false;
        }
        Cookie uuid = null;
        Cookie id = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.REMEMBER_ME_ID)) {
                id = cookie;
            }
            if (cookie.getName().equals(Constants.REMEMBER_ME_UUID)) {
                uuid = cookie;
            }
        }
        if (uuid == null || id == null) {
            return false;
        }
        try {
            RememberMeToken me = userService
                    .retrieveRememberMeToken(Long.parseLong(id.getValue()));
            if (RememberMeUtil.isValidToken(me)) {
                if (!req.getRemoteAddr().equals(me.getIp())) {
                    return false;
                }
                String uuidVal = uuid.getValue();
                String uuidExpected = me.getUuid().toString();
                if (uuidVal.equals(uuidExpected)) {
                    CustomUser user = me.getUser();
                    Set<GrantedAuthority> authorities = DefaultUserDetails
                            .mapAuthorities(user.getRoles());
                    UsernamePasswordAuthenticationToken token =
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(), user.getPassword(), authorities
                            );
                    authManager.authenticate(token, req);
                    return true;
                } else {
                    return false;
                }
            } else {
                userService.removeRememberMeToken(me.getId());
                return false;
            }
        } catch (NumberFormatException ex) {
            LOGGER.error("NUMBER FORMAT EXCEPTION : " + ex.getMessage());
            LOGGER.error(ex.getStackTrace());
        } catch (IOException e) {
            LOGGER.error("IOEXCEPTION IN AUTHENTICATION : " + e.getMessage());
            LOGGER.error(e.getStackTrace());
        }
        return false;
    }

    private ApplicationContext getContext(ServletContext servletContext) {
        if (context == null) {
            WebApplicationContext webApplicationContext = WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
            this.context = webApplicationContext;
        }
        return context;
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

    private SecurityContext retrieveSecurityContext(HttpSession session) {
        Object o = session.getAttribute(Constants.SPRING_SECURITY_CONTEXT_KEY);
        if (o != null && o instanceof SecurityContext) {
            return (SecurityContext) o;
        }
        return SecurityContextHolder.getContext();
    }
}
