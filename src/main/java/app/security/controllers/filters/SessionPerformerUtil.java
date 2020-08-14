package app.security.controllers.filters;

import app.configuration.spring.constants.Constants;
import app.models.basic.Performer;
import javax.servlet.http.HttpSession;

public class SessionPerformerUtil {
    public static Performer retrievePerformer(HttpSession session) {
        return (Performer)session
                .getAttribute(Constants.PERFORMER_SESSION_KEY);
    }
}
