package app.messenger.ws;

import app.security.wrappers.IAuthenticationWrapper;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component("determine_handshake_handler")
public class DetermineHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private IAuthenticationWrapper wrapper;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        return retrievePrincipal(request);
    }

    private Principal retrievePrincipal(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            if (((ServletServerHttpRequest) request).getServletRequest()
                    instanceof HttpServletRequest) {
                HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
                Object o = wrapper.getAuthentication(req);
                if (o instanceof Principal) {
                    return (Principal) o;
                } else {
                    o = wrapper.getPrincipal(req);
                    if (o instanceof Principal) {
                        return (Principal) o;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
