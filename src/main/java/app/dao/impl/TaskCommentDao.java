package app.dao.impl;

import app.dao.interfaces.ITaskCommentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.Task;
import app.models.basic.TaskComment;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class TaskCommentDao
        extends GenericJpaRepository<TaskComment>
        implements ITaskCommentDao {

    private static final String FROM =
            " select tc from TaskComment tc ";

    private static final String FIND_BY_TASK_ID =
            FROM + " WHERE task_id=:id ";

    private static final String FIND_BY_TASK_NAME =
            FROM + " INNER JOIN tc.task t ON t.toDo = :name ";

    public TaskCommentDao() {
        setClazz(TaskComment.class);
    }

    @Override
    public List<TaskComment> retrieveByTask(Task task) {
        return retrieveByTaskId(task.getId());
    }

    @Override
    public List<TaskComment> retrieveByTaskName(String taskName) {
        TypedQuery<TaskComment> query = getEntityManager()
                .createQuery(FIND_BY_TASK_NAME, TaskComment.class);
        query.setParameter("name", taskName);
        return query.getResultList();
    }

    @Override
    public List<TaskComment> retrieveByTaskId(Long id) {
        TypedQuery<TaskComment> query = getEntityManager()
                .createQuery(FIND_BY_TASK_ID, TaskComment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
