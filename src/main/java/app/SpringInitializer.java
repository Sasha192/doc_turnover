package app;

import app.configuration.spring.SpringDataConfiguration;
import app.configuration.spring.SpringMvcConfiguration;
import app.security.controllers.filters.AccessFilter;
import app.security.controllers.filters.AuthenticationFilter;
import app.security.controllers.filters.BlockRequestFilter;
import app.security.models.SimpleRole;
import javax.servlet.Filter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
                SpringDataConfiguration.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                SpringMvcConfiguration.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
                new BlockRequestFilter(),
                new AuthenticationFilter(),
                new AccessFilter("/performers",
                        SimpleRole.ADMIN,
                        SimpleRole.G_MANAGER,
                        SimpleRole.MANAGER),
                new AccessFilter("/departments",
                        SimpleRole.ADMIN,
                        SimpleRole.G_MANAGER),
                new AccessFilter("/roles",
                        SimpleRole.ADMIN,
                        SimpleRole.G_MANAGER)};
    }

    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        final DispatcherServlet dispatcherServlet =
                (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }
}
