package app.configuration.spring.hibernateinterceptors;

import org.hibernate.EmptyInterceptor;
import org.springframework.stereotype.Component;

@Component("hibernate_empty_int_impl")
public class HibernateEmptyInterceptorImpl extends EmptyInterceptor {

    /*@Override
    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) {
        if (entity instanceof Task) {

        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }*/
}
