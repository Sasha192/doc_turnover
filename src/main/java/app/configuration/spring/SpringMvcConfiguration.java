package app.configuration.spring;

import app.configuration.spring.constants.Constants;
import app.security.service.impl.DefaultUserDetailsService;
import app.security.utils.DefaultPasswordEncoder;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScans({
        @ComponentScan("app.controllers"),
        @ComponentScan("app.security.controllers"),
        @ComponentScan("app.security.utils"),
        @ComponentScan("app.security.wrappers"),
        @ComponentScan("app.tenantdefault")
})
@EnableScheduling
@EnableWebMvc
@EnableWebSecurity
public class SpringMvcConfiguration
        extends WebSecurityConfigurerAdapter
        implements WebMvcConfigurer {

    public static final long MAX_SIZE_UPLOAD = 5 * 0b10000000000 * 0b10000000000;

    private static final Logger LOGGER = Logger.getLogger(SpringMvcConfiguration.class);

    private final DefaultUserDetailsService userDetailsService;

    private final DefaultPasswordEncoder defaultPasswordEncoder;

    private final Constants constants;

    @Autowired
    private Environment environment;

    public SpringMvcConfiguration(DefaultUserDetailsService userDetailsService,
                                  DefaultPasswordEncoder defaultPasswordEncoder,
                                  @Qualifier("app_constants") Constants constants) {
        this.userDetailsService = userDetailsService;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.constants = constants;
    }

    @Bean
    public ViewResolver viewResolver() {
        final HandlebarsViewResolver handlebarsViewResolver = new HandlebarsViewResolver();
        handlebarsViewResolver.setPrefix("WEB-INF/views");
        handlebarsViewResolver.setSuffix(".hbs");
        handlebarsViewResolver.setCache(false);
        return handlebarsViewResolver;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        final CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(SpringMvcConfiguration.MAX_SIZE_UPLOAD);
        return multipartResolver;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/scripts/**")
                .addResourceLocations("classpath:/static/scripts/")
                .setCachePeriod(60 * 60);
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/")
                .setCachePeriod(60 * 60);
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .setCachePeriod(60 * 60);
        registry.addResourceHandler("/libs/**")
                .addResourceLocations("classpath:/static/libs/")
                .setCachePeriod(60 * 60);
        registry.addResourceHandler("/icons/**")
                .addResourceLocations("classpath:/static/icons/")
                .setCachePeriod(60 * 60);
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(60 * 60);
    }

    @Bean("for.email.template")
    public Handlebars forEmailTemplate() {
        final TemplateLoader loader =
                new FileTemplateLoader(
                        constants
                                .get("file_template_loader_base_dir")
                                .getStringValue(),
                        ".hbs");
        return new Handlebars(loader);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService);
        authProvider.setPasswordEncoder(this.defaultPasswordEncoder);
        return authProvider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authProvider());
        super.configure(auth);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .httpBasic();
    }
}