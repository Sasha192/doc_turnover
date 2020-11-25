package app.security.wrappers.impl;

import app.configuration.spring.constants.Constants;
import app.security.models.auth.CustomUser;
import app.security.models.auth.UserInfo;
import app.security.service.IUserService;
import app.security.wrappers.IAuthenticationViaCookies;
import app.security.wrappers.ICustomUserWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import app.tenantconfiguration.TenantContext;
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
        HttpSession session = request.getSession(true);
        if (session != null) {
            Object o = session.getAttribute(Constants.CUSTOM_USER_SESSION_KEY);
            if (o != null) {
                CustomUser user = (CustomUser) o;
                return user;
            }
        }
        String prev = TenantContext.getTenant();
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        CustomUser user = authCookies.authenticatedViaCookies(request);
        TenantContext.setTenant(prev != null ? prev : TenantContext.PHANTOM_TENANT_IDENTIFIER);
        return user;
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

    @Override
    public void update(UserInfo info) {
        userService.updateUserInfo(info);
    }
}
