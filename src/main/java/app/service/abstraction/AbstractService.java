package app.service.abstraction;

import app.dao.persistance.IGenericDao;
import app.dao.persistance.IOperations;
import java.io.Serializable;
import java.util.List;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractService<T extends Serializable> implements IOperations<T> {

    @Override
    @Transactional(readOnly = true)
    public T findOne(final long id) {
        return getDao().findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return (getDao().findAll());
    }

    @Transactional(readOnly = true)
    public List<T> findAll(int pageNumber, int pageSize) {
        return getDao().findAll(pageNumber, pageSize);
    }

    @Override
    @Transactional
    public T create(final T entity) {
        return getDao().create(entity);
    }

    @Override
    public void create(List<T> entities) {
        getDao().create(entities);
    }

    @Override
    @Transactional
    public T update(final T entity) {
        return getDao().update(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        getDao().delete(entity);
    }

    @Override
    @Transactional
    public void deleteById(long entityId) {
        T entity = findOne(entityId);
        delete(entity);
    }

    @Transactional(readOnly = true)
    public List<T> findBy(Predicate... predicates) {
        return getDao().findBy(predicates);
    }

    @Transactional(readOnly = true)
    public List<T> sortBy(Order... orders) {
        return getDao().sortBy(orders);
    }

    @Transactional(readOnly = true)
    public List<T> findSeveralById(Long... ids) {
        return getDao().findSeveralById(ids);
    }

    protected abstract IGenericDao<T> getDao();

}
