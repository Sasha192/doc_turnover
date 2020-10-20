package app.security.wrappers;

import app.customtenant.models.basic.Performer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface IPerformerWrapper {

    public Performer retrievePerformer(HttpServletRequest request);

    public Performer setPerformer(Performer performer, HttpSession session);

}
