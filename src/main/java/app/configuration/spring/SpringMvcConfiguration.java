package app.configuration.spring;

import com.github.jknack.handlebars.springmvc.HandlebarsView;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "app.controllers"
})
public class SpringMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public ViewResolver viewResolver() {
        HandlebarsViewResolver viewResolver = new HandlebarsViewResolver();
        viewResolver.setViewClass(HandlebarsView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".hbs");
        return viewResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        viewControllerRegistry.addViewController("/").setViewName("index");
    }
}
