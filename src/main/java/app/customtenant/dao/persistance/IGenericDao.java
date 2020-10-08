package app.customtenant.dao.persistance;

import java.io.Serializable;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface IGenericDao<T extends Serializable> extends IOperations<T> {
    //
    Root<T> getRoot();

    CriteriaQuery<T> getCriteriaQuery();

    List<T> findBy(Predicate... predicates);

    List<T> sortBy(Order... orders);

    CriteriaBuilder getCriteriaBuilder();
}
