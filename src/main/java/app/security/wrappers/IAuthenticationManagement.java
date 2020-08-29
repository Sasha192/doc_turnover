package app.security.wrappers;

import app.security.models.UserDto;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface IAuthenticationManagement {

    void authenticate(HttpServletRequest request,
                      HttpServletResponse response,
                      UserDto dto) throws IOException;

    void authenticate(UsernamePasswordAuthenticationToken token,
                      HttpServletRequest request) throws IOException;

    void invalidate(HttpServletRequest request, HttpServletResponse res)
            throws IOException;
}
