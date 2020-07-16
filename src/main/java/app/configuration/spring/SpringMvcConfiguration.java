package app.configuration.spring;

import app.security.service.impl.DefaultUserDetailsService;
import app.security.utils.DefaultAuthenticationFailureHandler;
import app.security.utils.DefaultAuthenticationSuccessHandler;
import app.security.utils.DefaultPasswordEncoder;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScans(value = {
        @ComponentScan("app.controllers"),
        @ComponentScan("app.security.controllers"),
        @ComponentScan("app.security.utils")
})
@EnableWebMvc
@EnableWebSecurity
public class SpringMvcConfiguration
        extends WebSecurityConfigurerAdapter
        implements WebMvcConfigurer {

    private static final int MAX_SIZE_UPLOAD = 20_000_000;

    private static final Logger LOGGER = Logger.getLogger(SpringMvcConfiguration.class);

    @Autowired
    private DefaultUserDetailsService userDetailsService;

    @Autowired
    private DefaultPasswordEncoder defaultPasswordEncoder;

    @Autowired
    @Qualifier("default_access_denied_handler")
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    @Qualifier("default.auth.success.handler")
    private DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;

    @Autowired
    @Qualifier("default.auth.failure.handler")
    private DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler;

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

    @Bean("for.email.template")
    public Handlebars forEmailTemplate() {
        TemplateLoader loader = new ClassPathTemplateLoader("/WEB-INF/templates", ".hbs");
        // verification_email_template.hbs
        return new Handlebars(loader);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(defaultPasswordEncoder);
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().and()
                .authorizeRequests()
                .antMatchers("/god/admin/*").hasRole("ADMIN")
                .antMatchers(
                        HttpMethod.GET,
                        "/WEB-INF/views/**", "/static/**", "/*.js", "/*.json", "/*.ico")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/auth")
                .defaultSuccessUrl("/profile", true)
                //.failureUrl("/denied")
                .successHandler(defaultAuthenticationSuccessHandler)
                .failureHandler(defaultAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout/perform")
                .logoutSuccessUrl("/logout/success")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }
}
