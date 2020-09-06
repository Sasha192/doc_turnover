package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.Task;
import app.models.basic.TaskStatus;
import java.util.List;

public interface ITaskDao extends IGenericDao<Task> {

    List<Task> findByPerformerId(long id);

    List<Task> findByPerformerIdStaticStatus(long id);

    void updateNameDescription(String newName, String description, Long taskId);

    List<Task> findOnDeadlineDate(int pageNumber, int pageSize);

    Integer countOnTaskStatus(long perfId, TaskStatus status);
}
