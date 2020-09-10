package app.configuration.spring.appfactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFactory {

    private static ApplicationContext context;

    @Autowired
    public ApplicationFactory(ApplicationContext acontext) {
        context = acontext;
    }

    public static Object getBean(Class clazz) {
        if (context != null) {
            return context.getBean(clazz);
        }
        return null;
    }

}
