package app.dao.persistance;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class AbstractJpaDao<T extends Serializable> {

    private static final String FROM = "from ";
    private static final int BATCH_SIZE = 5;
    protected String tableName;
    private String deleteById;
    private Class<T> clazz;
    private CriteriaBuilder criteriaBuilder;

    @PersistenceContext
    private EntityManager entityManager;

    public final void setClazz(final Class<T> clazzToSet) {
        this.clazz = clazzToSet;
        deleteById = FROM + clazz.getName() + " WHERE id=:id_ ";
    }

    public T findOne(final long id) {
        return entityManager.find(clazz, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery(FROM + clazz.getName()).getResultList();
    }

    public List<T> findAll(int pageNumber, int pageSize) {
        Query q = getEntityManager().createQuery(FROM + getClazz().getName());
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);
        return q.getResultList();
    }

    public T create(final T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public void create(final List<T> entities) {
        EntityManager em = getEntityManager();
        int c = 0;
        for (T entity : entities) {
            em.persist(entity);
            c++;
            if (c % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();;
    }

    public T update(final T entity) {
        return entityManager.merge(entity);
    }

    public void delete(final T entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final long entityId) {
        if (deleteById != null) {
            getEntityManager()
                    .createQuery(deleteById)
                    .setParameter("id_", entityId)
                    .executeUpdate();
        } else {
            final T entity = findOne(entityId);
            delete(entity);
        }
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        if (this.criteriaBuilder == null) {
            criteriaBuilder = entityManager.getCriteriaBuilder();
        }
        return this.criteriaBuilder;
    }

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

    public CriteriaQuery<T> getCriteriaQuery() {
        return this.getCriteriaBuilder().createQuery(getClazz());
    }

    public Root<T> getRoot() {
        return getCriteriaQuery().from(getClazz());
    }

    public List<T> findSeveralById(Long... ids) {
        if (ids == null || ids.length == 0 || ids.length > 6) {
            return null;
        }
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(getClazz());
        Root<T> c = q.from(getClazz());
        return getEntityManager()
                .createQuery(q.select(c).where(c.get("id").in(ids))).getResultList();
    }
}
