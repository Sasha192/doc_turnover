package app.tenantconfiguration;

import app.configuration.spring.constants.Constants;
import app.security.wrappers.CustomRequestContextHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class TenantSessionResolver implements ITenantSessionResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object tenantId = request.getParameter("tenant_id");
        if (tenantId != null && tenantId instanceof String) {
            return (String) tenantId;
        }
        tenantId = session.getAttribute(Constants.TENANT_SESSION_ID);
        if (tenantId != null && tenantId instanceof String) {
            return (String) tenantId;
        }
        return null;
    }

    @Override
    public String resolve() {
        HttpServletRequest req = CustomRequestContextHolder.getRequest();
        return resolve(req);
    }
}
