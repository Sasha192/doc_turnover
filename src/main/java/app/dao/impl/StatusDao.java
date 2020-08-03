package app.dao.impl;

import app.dao.interfaces.IStatusDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.TaskStatus;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class StatusDao extends GenericJpaRepository<TaskStatus>
        implements IStatusDao {

    private static final String FIND_BY_PERFORMER_ID =
            "from TaskStatus WHERE performer_id=:id";

    private static final String FIND_BY_TITLE_AND_PERF_ID =
            FIND_BY_PERFORMER_ID
            + " AND name=:title ";

    private static final String FIND_BY_TITLE =
            " from TaskStatus WHERE name=:title ";

    public StatusDao() {
        setClazz(TaskStatus.class);
    }

    @Override
    public List<TaskStatus> findByPerformerId(long id) {
        TypedQuery<TaskStatus> query = getEntityManager()
                .createQuery(FIND_BY_PERFORMER_ID, TaskStatus.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public TaskStatus findByTitleAndPerformer(Long performerId,
                                              String title) {
        TypedQuery<TaskStatus> query = getEntityManager()
                .createQuery(FIND_BY_TITLE_AND_PERF_ID, TaskStatus.class);
        query.setParameter("id", performerId);
        query.setParameter("title", title);
        List<TaskStatus> list = query.getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TaskStatus findByTitle(String title) {
        TypedQuery<TaskStatus> query = getEntityManager()
                .createQuery(FIND_BY_TITLE, TaskStatus.class);
        query.setParameter("title", title);
        List<TaskStatus> list = query.getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
