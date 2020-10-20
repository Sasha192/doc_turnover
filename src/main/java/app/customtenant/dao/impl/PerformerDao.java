package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.IPerformerDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.basic.Performer;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class PerformerDao extends GenericJpaRepository<Performer>
        implements IPerformerDao {

    private static final String WHERE_USER_ID =
            "select perf from Performer perf "
                    + " WHERE perf.userId = :user_id ";

    private static final String WHERE_DEPARTMENT_ID =
            " from Performer perf WHERE perf.department.id = :department_id_ ";

    public PerformerDao() {
        setClazz(Performer.class);
    }

    @Override
    public List<Performer> findByDepartmentId(Long departmentId) {
        TypedQuery<Performer> typedQuery = getEntityManager()
                .createQuery(WHERE_DEPARTMENT_ID, Performer.class);
        typedQuery.setParameter("department_id_", departmentId);
        return typedQuery.getResultList();
    }

    @Override
    public Performer retrieveByUserId(Long id) {
        TypedQuery<Performer> typedQuery = getEntityManager()
                .createQuery(WHERE_USER_ID, Performer.class);
        typedQuery.setParameter("user_id", id);
        return typedQuery.getSingleResult();
    }
}
