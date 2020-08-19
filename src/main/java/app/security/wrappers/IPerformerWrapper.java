package app.security.wrappers;

import app.models.basic.Performer;
import javax.servlet.http.HttpServletRequest;

public interface IPerformerWrapper {

    public Performer retrievePerformer(HttpServletRequest request);

}
