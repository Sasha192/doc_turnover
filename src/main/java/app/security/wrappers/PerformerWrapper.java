package app.security.wrappers;

import app.customtenant.models.basic.Performer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class PerformerWrapper implements IPerformerWrapper {
    @Override
    public Performer retrievePerformer(HttpServletRequest request) {
        return null;
    }
}
