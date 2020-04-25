package app.dao.persistance;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GenericJpaRepository<T extends Serializable>
        extends AbstractJpaDao<T> implements IGenericDao<T> {

    public List<T> findBy(Predicate... predicates) {
        CriteriaQuery<T> criteriaQuery = this.getCriteriaBuilder().createQuery(getClazz());
        criteriaQuery.where(predicates);
        return this.getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> sortBy(Order... orders) {
        CriteriaQuery<T> criteriaQuery = this.getCriteriaBuilder().createQuery(getClazz());
        criteriaQuery.orderBy(orders);
        return this.getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> findAll(int pageNumber, int pageSize) {
        Query q = getEntityManager().createQuery("from " + getClazz().getName());
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);
        return q.getResultList();
    }
}
