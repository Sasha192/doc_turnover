package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.ITaskDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDao extends GenericJpaRepository<Task>
        implements ITaskDao {

    private static final String HQL_FROM =
            " SELECT t from Task t ";

    private static final String MYSQL_FROM =
            "SELECT t.id, t.creation_time, t.is_deadline, "
                    + "               t.task, t.deadline, t.control_date, "
                    + "               t.status_id, t.task_owner_id, "
                    + "               t.description, t.report_id "
                    + "FROM tasks t ";

    private static final String FIND_BY_PERFORMER_ID =
                        MYSQL_FROM
                    + " INNER JOIN tasks_performers tp on t.id = tp.task_id "
                    + " WHERE tp.performer_id = :_perf_id ";

    private static final String FIND_BY_DEPARTMENT_ID =
                    MYSQL_FROM
                    + " INNER JOIN tasks_performers tp on t.id = tp.task_id "
                    + " INNER JOIN performers p on tp.performer_id = p.id "
                    + " WHERE p.department_id = :depo_id ";

    private static final String FIND_BY_STATUS =
            HQL_FROM
            + " WHERE t.status = :status_ ";

    private static final String FIND_BY_PEFORMER_N_STATUS =
                    FIND_BY_PERFORMER_ID
                    + " AND t.status_id = :_status_ ";

    private static final String UPDATE_NAME_DESCRIPTION =
            "UPDATE Task t SET t.name = :name, t.description = :descript ";

    private static final String SELECT_ALL_DEADLINE_DATE_AND_DEADLINE_FALSE =
            "SELECT * FROM tasks WHERE is_deadline = false AND "
                    + " deadline < CURRENT_TIMESTAMP "
                    + " LIMIT :page_size_ OFFSET :offset_";

    public TaskDao() {
        setClazz(Task.class);
    }

    private List<Task> offsetLimitQuery(int page, int pageSize, Query query) {
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<Task> findByPerformerId(int page, int pageSize, long perfId) {
        Query query = getEntityManager()
                .createNativeQuery(FIND_BY_PERFORMER_ID, Task.class);
        query.setParameter("_perf_id", perfId);
        return offsetLimitQuery(page, pageSize, query);
    }

    @Override
    public List<Task> findByDepartment(int pageId, int pageSize, long departmentId) {
        Query query = getEntityManager()
                .createNativeQuery(FIND_BY_DEPARTMENT_ID, Task.class);
        query.setParameter("depo_id", departmentId);
        return offsetLimitQuery(pageId, pageSize, query);
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
    public List<Task> findByStatus(int page, int pageSize, TaskStatus status) {
        Query query = getEntityManager()
                .createQuery(FIND_BY_STATUS, Task.class);
        query.setParameter("status_", status);
        return offsetLimitQuery(page, pageSize, query);
    }

    @Override
    public List<Task> findByPerformerAndStatus(
            int page, int pageSize, long performerId, TaskStatus status) {
        Query query = getEntityManager()
                .createNativeQuery(FIND_BY_PEFORMER_N_STATUS, Task.class);
        query.setParameter("_perf_id", performerId);
        query.setParameter("_status_", status);
        return offsetLimitQuery(page, pageSize, query);
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

}
