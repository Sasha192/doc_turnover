package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IPerformerService;
import app.customtenant.service.interfaces.IPerformerUpdateEventListenerService;
import app.security.models.auth.CustomUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import app.security.models.auth.UserInfo;
import app.tenantconfiguration.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerformerWrapper implements IPerformerWrapper {

    private final IPerformerService performerService;

    private final ICustomUserWrapper userWrapper;

    private final IPerformerUpdateEventListenerService performerUpdateService;

    public PerformerWrapper(IPerformerUpdateEventListenerService performerUpdateService,
                            IPerformerService performerService,
                            ICustomUserWrapper userWrapper) {
        this.performerUpdateService = performerUpdateService;
        this.performerService = performerService;
        this.userWrapper = userWrapper;
    }

    @Override
    public Performer retrievePerformer(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Object o = session.getAttribute(Constants.PERFORMER_SESSION_KEY);
        if (o == null) {
            return setPerformer(request, session);
        }
        Performer performer = (Performer) o;
        if (performerUpdateService.toUpdate(performer.getId())) {
            performer = setPerformer(request, session);
            performerUpdateService.wasUpdated(performer.getId());
        }
        return performer;
    }

    private Performer setPerformer(HttpServletRequest req, HttpSession session) {
        CustomUser user = userWrapper.retrieveUser(req);
        UserInfo info = user.getUserInfo();
        String activeTenant = info.getActiveTenant();
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
