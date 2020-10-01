package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.models.basic.CustomUser;
import app.security.models.DefaultUserDetails;
import app.security.models.RememberMeToken;
import app.security.service.IUserService;
import app.security.utils.RememberMeUtil;
import java.io.IOException;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auth_manage_wrapper")
public class AuthenticationManagementWrapper implements
        IAuthenticated,
        IRequestAuthority {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private static final String CLASS_NAME = AuthenticationManagementWrapper.class.getName();

    private IAuthenticationWrapper authWrapper;

    private IAuthenticationManagement authManager;

    private IUserService userService;

    @Autowired
    public AuthenticationManagementWrapper(IAuthenticationWrapper authWrapper,
                                           IAuthenticationManagement authManager,
                                           IUserService userService) {
        this.authWrapper = authWrapper;
        this.authManager = authManager;
        this.userService = userService;
    }

    @Override
    public boolean isAuthenticated(HttpServletRequest req) {
        Authentication auth = retrieveAuthentication(req);
        if (auth != null && auth.isAuthenticated()) {
            return true;
        } else {
            if (authenticatedViaCookies(req)) {
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean hasAuthority(HttpServletRequest request,
                                SimpleGrantedAuthority authority) {
        Authentication auth = retrieveAuthentication(request);
        if (auth.getAuthorities().contains(authority)) {
            return true;
        }
        return false;
    }

    private Authentication retrieveAuthentication(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session != null) {
            SecurityContext context = retrieveSecurityContext(session);
            if (context != null) {
                Authentication auth = context.getAuthentication();
                return auth;
            }
        }
        return null;
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
            if (me == null) {
                return false;
            }
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
            LOGGER.error(CLASS_NAME + "\nNUMBER FORMAT EXCEPTION : "
                    + ex.getMessage());
            LOGGER.error(ex.getStackTrace());
        } catch (IOException e) {
            LOGGER.error(CLASS_NAME + "\nIOEXCEPTION IN AUTHENTICATION : "
                    + e.getMessage());
            LOGGER.error(e.getStackTrace());
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
