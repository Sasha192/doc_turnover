package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.IBriefPerformerDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.mysqlviews.BriefPerformer;
import app.security.models.auth.CustomUser;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class BriefPerformerDao
        extends GenericJpaRepository<BriefPerformer>
        implements IBriefPerformerDao {

    private static final String WHERE_DEPARTMENT_ID =
            " from BriefPerformer bperf "
                    + "INNER JOIN Performer perf "
                    + "ON perf.department.id = :department_id_ ";

    public BriefPerformerDao() {
        setClazz(BriefPerformer.class);
    }

    @Override
    public BriefPerformer retrieveByUserDetails(UserDetails userDetails) {
        return null;
    }

    @Override
    public BriefPerformer retrieveByUser(CustomUser user) {
        return null;
    }

    @Override
    public BriefPerformer retrieveByUsername(String username) {
        return null;
    }

    @Override
    public List<BriefPerformer> findByDepartmentId(Long departmentId) {
        TypedQuery<BriefPerformer> typedQuery = getEntityManager()
                .createQuery(WHERE_DEPARTMENT_ID, BriefPerformer.class);
        typedQuery.setParameter("department_id_", departmentId);
        return typedQuery.getResultList();
    }
}
