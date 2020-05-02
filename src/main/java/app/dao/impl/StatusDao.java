package app.dao.impl;

import app.dao.IStatusDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.TaskStatus;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class StatusDao extends GenericJpaRepository<TaskStatus>
        implements IStatusDao {

    private static final String FIND_BY_PERFORMER_ID =
            "from Status WHERE performer_id=:id";

    public StatusDao() {
        setClazz(TaskStatus.class);
    }

    @Override
    public List<TaskStatus> findByPerformerId(Integer id) {
        TypedQuery<TaskStatus> query = getEntityManager()
                .createQuery(FIND_BY_PERFORMER_ID, TaskStatus.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
