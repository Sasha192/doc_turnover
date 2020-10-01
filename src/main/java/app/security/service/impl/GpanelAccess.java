package app.security.service.impl;

import app.security.service.IGpanelCheckPermission;
import app.security.wrappers.IRequestAuthority;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Qualifier("gpanel_access")
public class GpanelAccess implements IGpanelCheckPermission {

    private static final SimpleGrantedAuthority admin =
            new SimpleGrantedAuthority("ROLE_ADMIN");

    @Autowired
    private IRequestAuthority requestAuthority;

    @Override
    public boolean check(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String uri = request.getRequestURI();
        if (uri.startsWith("/gpanel")) {
            if (requestAuthority.hasAuthority(request, admin)) {
                return true;
            }
        }
        return false;
    }
}
