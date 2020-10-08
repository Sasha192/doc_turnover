package app.customtenant.dao.persistance;

import java.io.Serializable;
import java.util.List;

public interface IOperations<T extends Serializable> {
    T findOne(final long id);

    List<T> findAll();

    List<T> findAll(int pageNumber, int pageSize);

    T create(final T entity);

    void create(final List<T> entities);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final long entityId);

    List<T> findSeveralById(Long... ids);
}
