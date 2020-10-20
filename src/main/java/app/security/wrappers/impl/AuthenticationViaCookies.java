package app.security.wrappers.impl;

import app.configuration.spring.constants.Constants;
import app.security.models.auth.CustomUser;
import app.security.models.auth.RememberMeToken;
import app.security.service.IUserService;
import app.security.utils.RememberMeUtil;
import app.security.wrappers.IAuthenticationManagement;
import app.security.wrappers.IAuthenticationViaCookies;
import app.security.wrappers.IAuthenticationWrapper;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationViaCookies
        implements IAuthenticationViaCookies {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private static final String CLASS_NAME = AuthenticationViaCookies.class.getName();

    private IAuthenticationWrapper authWrapper;

    private IAuthenticationManagement authManager;

    private IUserService userService;

    @Autowired
    public AuthenticationViaCookies(IAuthenticationWrapper authWrapper,
                                    IAuthenticationManagement authManager,
                                    IUserService userService) {
        this.authWrapper = authWrapper;
        this.authManager = authManager;
        this.userService = userService;
    }

    @Override
    public CustomUser authenticatedViaCookies(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
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
            return null;
        }
        try {
            RememberMeToken me = userService.retrieveRememberMeToken(
                    Long.parseLong(id.getValue())
            );
            if (me == null) {
                return null;
            }
            if (RememberMeUtil.isValidToken(me)) {
                if (!req.getRemoteAddr().equals(me.getIp())) {
                    return null;
                }
                String uuidVal = uuid.getValue();
                String uuidExpected = me.getUuid().toString();
                if (uuidVal.equals(uuidExpected)) {
                    CustomUser user = me.getUser();
                    authManager.authenticate(user, req);
                    return user;
                } else {
                    return null;
                }
            } else {
                userService.removeRememberMeToken(me.getId());
                return null;
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
        return null;
    }
}
