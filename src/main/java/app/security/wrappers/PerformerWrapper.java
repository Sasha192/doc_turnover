package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.auth.CustomUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import app.security.models.auth.UserInfo;
import app.tenantconfiguration.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerformerWrapper implements IPerformerWrapper {

    @Autowired
    private IPerformerService performerService;

    @Autowired
    private ICustomUserWrapper userWrapper;

    @Override
    public Performer retrievePerformer(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Object o = session.getAttribute(Constants.PERFORMER_SESSION_KEY);
        if (o == null) {
            return setPerformer(request, session);
        }
        return (Performer) o;
    }

    private Performer setPerformer(HttpServletRequest req, HttpSession session) {
        CustomUser user = userWrapper.retrieveUser(req);
        if (user == null) {
            return null;
        }
        UserInfo info = user.getUserInfo();
        String activeTenant = (String) session.getAttribute(Constants.TENANT_SESSION_ID);
        String prevTenant = TenantContext.getTenant();
        TenantContext.setTenant(activeTenant);
        Performer performer = performerService.retrieveByUserId(user.getId());
        session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
        TenantContext.setTenant(prevTenant);
        return performer;
    }

    @Override
    public Performer setPerformer(Performer performer, HttpSession session) {
        session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
        return performer;
    }
}
