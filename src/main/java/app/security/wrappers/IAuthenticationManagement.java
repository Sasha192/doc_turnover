package app.security.wrappers;

import app.security.models.UserDto;
import app.security.models.auth.CustomUser;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAuthenticationManagement {

    void authenticate(HttpServletRequest request,
                      HttpServletResponse response,
                      UserDto dto) throws IOException;

    /**
     *  U should never, ever pass anything excepty UsernamePasswordAuthenticationToken or
     *  SecurityContextImpl should always hold UsernamePasswordAuthenticationToken
     * @see app.customtenant.messenger.ws.AuthenticationWsInterceptor
     * #retrieveUser(StompHeaderAccessor)
     */
    void authenticate(CustomUser user,
                      HttpServletRequest request) throws IOException;

    void invalidate(HttpServletRequest request, HttpServletResponse res)
            throws IOException;
}
