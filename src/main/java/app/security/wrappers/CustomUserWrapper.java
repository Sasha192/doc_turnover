package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.security.models.auth.CustomUser;
import app.security.service.IUserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomUserWrapper implements ICustomUserWrapper {

    @Autowired
    private IAuthenticationViaCookies authCookies;

    @Autowired
    private IUserService userService;

    @Override
    public CustomUser retrieveUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object o = session.getAttribute(Constants.CUSTOM_USER_SESSION_KEY);
            if (o != null) {
                CustomUser user = (CustomUser) o;
                return user;
            } else {
                return null;
            }
        }
        return authCookies.authenticatedViaCookies(request);
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
