package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.auth.CustomUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        Performer performer = null;
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
        Performer performer = performerService.retrieveByUserId(user.getId());
        session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
        return performer;
    }

    @Override
    public Performer setPerformer(Performer performer, HttpSession session) {
        session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
        return performer;
    }
}
