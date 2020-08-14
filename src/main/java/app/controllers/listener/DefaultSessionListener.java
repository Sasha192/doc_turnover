package app.controllers.listener;

import app.configuration.spring.constants.Constants;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultSessionListener implements HttpSessionListener {

    // @TODO : Logging sessions

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent
                .getSession()
                .setMaxInactiveInterval(
                        Constants.MAX_INACTIVE_SESSION_INTERVAL_SECONDS
                );
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ;
    }
}
