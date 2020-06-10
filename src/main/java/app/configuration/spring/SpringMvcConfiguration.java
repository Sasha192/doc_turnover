package app.configuration.spring;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "app.controllers"
})
public class SpringMvcConfiguration implements WebMvcConfigurer {

    private static final int MAX_SIZE_UPLOAD = 20_000_000;

    private static final Logger LOGGER = Logger.getLogger(SpringMvcConfiguration.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public ViewResolver viewResolver() {
        HandlebarsViewResolver handlebarsViewResolver = new HandlebarsViewResolver();
        handlebarsViewResolver.setPrefix("/WEB-INF/views/");
        handlebarsViewResolver.setSuffix(".hbs");
        handlebarsViewResolver.setCache(false);
        return handlebarsViewResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        viewControllerRegistry.addViewController("/")
                .setViewName("index");
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(MAX_SIZE_UPLOAD);
        return multipartResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("scripts/**")
                .addResourceLocations("classpath:/static/scripts/");
        registry.addResourceHandler("fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("img/**")
                .addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("libs/**")
                .addResourceLocations("classpath:/static/libs/");
        registry.addResourceHandler("css/**")
                .addResourceLocations("classpath:/static/css/");
    }
}
