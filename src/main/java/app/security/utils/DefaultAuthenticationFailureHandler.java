package app.security.utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component("default.auth.failure.handler")
public class DefaultAuthenticationFailureHandler
        extends SimpleUrlAuthenticationFailureHandler {

    private static final String DEFAULT_FAILURE_URL = "/fail/auth";
    private static final String DELIMETER = " | ".intern();
    private static final Logger LOGGER = Logger.getLogger("authLogger");

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        LOGGER.error(DefaultAuthenticationSuccessHandler.class);
        LOGGER.error("AUTHENTICATION EXCEPTION " + DELIMETER + "\n" + exception.getMessage());
        super.setDefaultFailureUrl(DEFAULT_FAILURE_URL);
    }
}
