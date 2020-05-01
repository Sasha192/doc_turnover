package app.dao.impl;

import app.dao.IStatusDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.Status;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

@Repository
public class StatusDao extends GenericJpaRepository<Status>
        implements IStatusDao {

    private static final String FIND_BY_PERFORMER_ID =
            "from Status WHERE performer_id=:id";

    public StatusDao() {
        setClazz(Status.class);
    }

    @Override
    public List<Status> findByPerformerId(Integer id) {
        TypedQuery<Status> query = getEntityManager()
                .createQuery(FIND_BY_PERFORMER_ID, Status.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
