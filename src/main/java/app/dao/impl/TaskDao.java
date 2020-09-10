package app.dao.impl;

import app.dao.interfaces.ITaskDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.TaskStatus;
import app.models.basic.taskmodels.Task;
import app.utils.CustomAppDateTimeUtil;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDao extends GenericJpaRepository<Task>
        implements ITaskDao {

    private static final String FROM =
            "from Task t ";

    private static final String FIND_BY_PERFORMER_ID =
            FROM.concat("WHERE t.performer_id=:id ");

    private static final String FIND_BY_PERFORMER_ID_STATIC_STATUS =
            FROM.concat("INNER JOIN custom_status cs "
                    + "ON cs.is_custom=false AND t.performer_id=cs.performer_id ")
                    .concat(FIND_BY_PERFORMER_ID);

    private static final String UPDATE_NAME_DESCRIPTION =
            "UPDATE Task t SET t.toDo = :name, t.description = :descript ";

    private static final String SELECT_ALL_DEADLINE_DATE_AND_DEADLINE_FALSE =
            "SELECT * FROM tasks WHERE is_deadline = false AND "
                    + " deadline < CURRENT_TIMESTAMP "
                    + " LIMIT :page_size_ OFFSET :offset_";

    private static final String COUNT_ON_TASK_STATUS_PERF_ID =
            "SELECT COUNT(*) FROM task_performers tp "
                    + " INNER JOIN tasks t ON t.id=tp.task_id "
                    + " WHERE performer_id=:perf_id_ AND t.status_id=:status_id_";

    public TaskDao() {
        setClazz(Task.class);
    }

    @Override
    public List<Task> findByPerformerId(long id) {
        TypedQuery<Task> query = getEntityManager()
                .createQuery(FIND_BY_PERFORMER_ID, Task.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Task> findByPerformerIdStaticStatus(long id) {
        TypedQuery<Task> query = getEntityManager()
                .createQuery(FIND_BY_PERFORMER_ID_STATIC_STATUS, Task.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public Task update(Task entity) {
        entity.setModificationDate(CustomAppDateTimeUtil.now());
        return super.update(entity);
    }

    @Override
    public void updateNameDescription(String newName,
                                      String description,
                                      Long taskId) {
        getEntityManager().createQuery(UPDATE_NAME_DESCRIPTION)
                .setParameter("name", newName)
                .setParameter("descript", description)
                .executeUpdate();
    }

    @Override
    public List<Task> findOnDeadlineDate(int pageNumber, int pageSize) {
        if (pageNumber > 0) {
            int offset = (pageNumber - 1) * pageSize;
            Query query = getEntityManager()
                    .createNativeQuery(SELECT_ALL_DEADLINE_DATE_AND_DEADLINE_FALSE,
                            Task.class).setParameter("page_size_", pageSize)
                    .setParameter("offset_", offset);
            return query.getResultList();
        } else {
            throw new IllegalArgumentException("pageNumber MUST be GREATER THAN 0");
        }
    }

    @Override
    public Integer countOnTaskStatus(long perfId, TaskStatus status) {
        Object o = getEntityManager().createNativeQuery(COUNT_ON_TASK_STATUS_PERF_ID)
                .setParameter("perf_id_", perfId)
                .setParameter("status_id_", status.getId())
                .getSingleResult();
        if (o instanceof Number) {
            return (Integer) o;
        }
        throw new UnsupportedOperationException();
    }
}
