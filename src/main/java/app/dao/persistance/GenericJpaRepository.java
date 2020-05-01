package app.dao.persistance;

import java.io.Serializable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GenericJpaRepository<T extends Serializable>
        extends AbstractJpaDao<T> implements IGenericDao<T> {

}
