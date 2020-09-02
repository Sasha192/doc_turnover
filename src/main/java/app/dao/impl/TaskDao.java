package app.dao.impl;

import app.dao.interfaces.ITaskDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.Task;
import app.utils.CustomAppDateTimeUtil;
import java.util.List;
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
}
