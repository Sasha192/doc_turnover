package app.security.wrappers;

import app.customtenant.models.basic.Performer;
import javax.servlet.http.HttpServletRequest;

public interface IPerformerWrapper {

    public Performer retrievePerformer(HttpServletRequest request);

}
