package app.configuration.spring;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import javax.jms.ConnectionFactory;

@Configuration
public class JmsActiveMQConfiguration {

    private static final String DEFAULT_BROKER_URL = "tcp://127.0.0.1:61616";

    @Bean
    public ConnectionFactory connectionFactory() {
        org.apache.activemq.pool.PooledConnectionFactory pooled = new PooledConnectionFactory();
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setTrustAllPackages(true);
        pooled.setConnectionFactory(connectionFactory);
        pooled.setMaxConnections(8);
        return pooled;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory container1(ConcurrentTaskExecutor executor) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setTaskExecutor(executor);
        return factory;
    }
}
