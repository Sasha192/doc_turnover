package app.configuration.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScans({
        @ComponentScan("app.customtenant.messenger.ws")
})
public class WebSocketConfiguration
        implements WebSocketMessageBrokerConfigurer {

    @Autowired
    @Qualifier("auth_ws_interceptor")
    private ChannelInterceptor authInterceptor;

    @Autowired
    @Qualifier("determine_handshake_handler")
    private DefaultHandshakeHandler handshakeHandler;

    @Bean
    public HttpSessionHandshakeInterceptor getInterceptor() {
        return new HttpSessionHandshakeInterceptor();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/messenger/connect")
                .addInterceptors(getInterceptor())
                .setHandshakeHandler(handshakeHandler)
                .withSockJS()
                .setHeartbeatTime(60_000);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue", "/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }
}
