package app;

import app.configuration.spring.SpringDataConfiguration;
import app.configuration.spring.SpringMvcConfiguration;
import app.security.utils.AccessDepartment;
import app.security.utils.AccessGodAdminFilter;
import app.security.utils.AccessPerformer;
import app.security.utils.AuthenticationFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

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
        return new Filter[]{new AuthenticationFilter(), new AccessGodAdminFilter(),
                new AccessDepartment(), new AccessPerformer()};
    }
}
