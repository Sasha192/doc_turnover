package app.dao.impl;

import app.dao.interfaces.IPerformerDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.CustomUser;
import app.models.basic.Performer;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class PerformerDao extends GenericJpaRepository<Performer>
        implements IPerformerDao {

    private static final String WHERE_USERNAME =
            "select perf from Performer perf "
                    + " INNER JOIN perf.user u ON u.email = :email ";

    private static final String WHERE_DEPARTMENT_ID =
            " from Performer perf WHERE perf.department.id = :department_id_ ";

    public PerformerDao() {
        setClazz(Performer.class);
    }

    @Override
    public Performer retrieveByUserDetails(UserDetails userDetails) {
        return retrieveByUsername(userDetails.getUsername());
    }

    @Override
    public Performer retrieveByUser(CustomUser user) {
        return retrieveByUsername(user.getEmail());
    }

    @Override
    public Performer retrieveByUsername(String username) {
        List<Performer> performers = getEntityManager()
                .createQuery(WHERE_USERNAME, Performer.class)
                .setParameter("email", username)
                .getResultList();
        if (performers.isEmpty()) {
            return null;
        }
        return performers.get(0);
    }

    @Override
    public List<Performer> findByDepartmentId(Long departmentId) {
        TypedQuery<Performer> typedQuery = getEntityManager()
                .createQuery(WHERE_DEPARTMENT_ID, Performer.class);
        typedQuery.setParameter("department_id_", departmentId);
        return typedQuery.getResultList();
    }
}
