package app.security.utils;

import app.configuration.spring.constants.Constants;
import app.models.basic.CustomUser;
import app.security.models.RememberMeToken;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Component
public class RememberMeUtil {

    @Autowired
    private SessionLocaleResolver slr;

    public RememberMeToken getRememberMeToken(CustomUser user,
                                              HttpServletRequest request) {
        RememberMeToken token = new RememberMeToken();
        token.setUser(user);
        token.setCreationTime(System.currentTimeMillis());
        token.setUuid(UUID.randomUUID());
        token.setIp(request.getRemoteAddr());
        return token;
    }

    public static boolean isValidToken(RememberMeToken token) {
        int diff = (int) ((System.currentTimeMillis() - token.getCreationTime()) / (1_000));
        if (diff < Constants.VALID_REMEMBER_ME_TOKEN_TIME_SEC) {
            return true;
        } else {
            return false;
        }
    }

}
