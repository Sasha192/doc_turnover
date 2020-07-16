package app.dao.impl;

import app.dao.IPerformerDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.CustomUser;
import app.models.Performer;

import javax.persistence.TypedQuery;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PerformerDao extends GenericJpaRepository<Performer>
        implements IPerformerDao {

    private static final String WHERE_USERNAME =
            "from Performer perf"
                    + "INNER JOIN perf.user u where u.email = :email";

    private static final String WHERE_DEPARTMENT_ID =
            "from Performer perf WHERE perf.department.id = :department_id_";

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
        TypedQuery<Performer> typedQuery = getEntityManager()
                .createQuery(WHERE_USERNAME, Performer.class);
        typedQuery.setParameter("email", username);
        List<Performer> performers = typedQuery.getResultList();
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
