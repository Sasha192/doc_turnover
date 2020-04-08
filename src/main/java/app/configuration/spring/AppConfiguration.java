package app.configuration.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans(value = {
        @ComponentScan("app.configuration"),
})
public class AppConfiguration {



}
