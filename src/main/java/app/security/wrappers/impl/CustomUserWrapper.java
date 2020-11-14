package app.security.wrappers.impl;

import app.security.models.auth.CustomUser;
import app.security.service.IUserService;
import app.security.wrappers.IAuthenticationViaCookies;
import app.security.wrappers.ICustomUserWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomUserWrapper implements ICustomUserWrapper {

    @Autowired
    private IAuthenticationViaCookies authCookies;

    @Autowired
    private IUserService userService;

    @Override
    public @NotNull CustomUser retrieveUser(HttpServletRequest request) {
        return userService.findOne(1L);
        /*HttpSession session = request.getSession(false);
        if (session != null) {
            Object o = session.getAttribute(Constants.CUSTOM_USER_SESSION_KEY);
            if (o != null) {
                CustomUser user = (CustomUser) o;
                return user;
            } else {
                return null;
            }
        }
        return authCookies.authenticatedViaCookies(request);*/
    }

    @Override
    public CustomUser retrieveUser() {
        HttpServletRequest request = CustomRequestContextHolder.getRequest();
        return retrieveUser(request);
    }

    @Override
    public void update(CustomUser user) {
        userService.update(user);
    }
}
