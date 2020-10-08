package app.customtenant.messenger.ws;

import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

@Component("auth_ws_interceptor")
public class AuthenticationWsInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class
                );
        if (StompCommand.CONNECT == accessor.getCommand()) {
            UsernamePasswordAuthenticationToken user = retrieveUser(accessor);
            accessor.setUser(user);
        }
        if (!(StompCommand.CONNECT == accessor.getCommand())) {
            if (accessor.getUser() == null) {
                throw new AuthenticationCredentialsNotFoundException(
                        "COULD NOT FIND USER CREDENTIALS"
                );
            }
        }
        return message;
    }

    private UsernamePasswordAuthenticationToken retrieveUser(StompHeaderAccessor accessor) {
        Map map = accessor.getMessageHeaders();
        if (map != null) {
            if (map.containsKey("simpSessionAttributes")) {
                if (map.get("simpSessionAttributes") instanceof Map) {
                    map = (Map) map.get("simpSessionAttributes");
                    if (map.containsKey("SPRING_SECURITY_CONTEXT")) {
                        if (map.get("SPRING_SECURITY_CONTEXT") instanceof SecurityContext) {
                            SecurityContext ssc = (SecurityContext) map
                                    .get("SPRING_SECURITY_CONTEXT");
                            Authentication authentication = ssc.getAuthentication();
                            if (authentication != null
                                    && authentication
                                    instanceof UsernamePasswordAuthenticationToken) {
                                return (UsernamePasswordAuthenticationToken) authentication;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
