package app.dao.persistance;

import java.io.Serializable;
import java.util.List;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

public interface IOperations<T extends Serializable> {

    T findOne(final long id);

    List<T> findAll();

    T create(final T entity);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final long entityId);

    List<T> findBy(Predicate... predicates);

    List<T> sortBy(Order... orders);

    List<T> findAll(int pageNumber, int pageSize);
}
