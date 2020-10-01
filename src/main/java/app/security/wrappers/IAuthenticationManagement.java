package app.security.wrappers;

import app.security.models.UserDto;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface IAuthenticationManagement {

    void authenticate(HttpServletRequest request,
                      HttpServletResponse response,
                      UserDto dto) throws IOException;

    /**
     *  U should never, ever pass anything excepty UsernamePasswordAuthenticationToken or
     *  SecurityContextImpl should always hold UsernamePasswordAuthenticationToken
     * @see app.messenger.ws.AuthenticationWsInterceptor#retrieveUser(StompHeaderAccessor)
     */
    void authenticate(UsernamePasswordAuthenticationToken token,
                      HttpServletRequest request) throws IOException;

    void invalidate(HttpServletRequest request, HttpServletResponse res)
            throws IOException;
}
