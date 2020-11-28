package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.IPerformerDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.basic.Performer;
import app.security.models.SimpleRole;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class PerformerDao extends GenericJpaRepository<Performer>
        implements IPerformerDao {

    private static final String WHERE_USER_ID =
            "select perf from Performer perf "
                    + " WHERE perf.userId = :user_id ";

    private static final String WHERE_DEPARTMENT_ID =
            " from Performer perf WHERE perf.departmentId = :department_id_ ";

    private static final String UPDATE_PERF_DEPO =
            " update Performer perf set perf.departmentId = :depo_id "
                    + "where perf.id = :id_ ";

    private static final String UPDATE_PERF_ROLE =
            " update Performer perf set perf.role = :role_id "
                    + "where perf.id = :id_ ";

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
        try {
            TypedQuery<Performer> typedQuery = getEntityManager()
                    .createQuery(WHERE_USER_ID, Performer.class);
            typedQuery.setParameter("user_id", id);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public int updatePerformerDepartment(long perfId, long depoId) {
        return getEntityManager().createQuery(UPDATE_PERF_DEPO)
                .setParameter("depo_id", depoId)
                .setParameter("id_", perfId)
                .executeUpdate();
    }

    @Override
    public int updatePerformerRole(Long performerId, SimpleRole role) {
        return getEntityManager().createQuery(UPDATE_PERF_ROLE)
                .setParameter("role_id", role)
                .setParameter("id_", performerId)
                .executeUpdate();
    }
}
