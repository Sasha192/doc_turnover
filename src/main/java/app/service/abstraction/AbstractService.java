package app.service.abstraction;

import app.dao.persistance.IGenericDao;
import app.dao.persistance.IOperations;
import com.google.common.collect.Lists;
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
        return Lists.newArrayList(getDao().findAll());
    }

    @Transactional(readOnly = true)
    public List<T> findAll(int pageNumber, int pageSize) {
        return getDao().findAll(pageNumber, pageSize);
    }

    @Override
    public T create(final T entity) {
        return getDao().create(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().update(entity);
    }

    @Override
    public void delete(T entity) {
        getDao().delete(entity);
    }

    @Override
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

    protected abstract IGenericDao<T> getDao();

}
