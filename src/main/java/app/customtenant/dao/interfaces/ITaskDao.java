package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import java.util.List;

public interface ITaskDao extends IGenericDao<Task> {

    List<Task> findByPerformerId(long id);

    List<Task> findByPerformerIdStaticStatus(long id);

    void updateNameDescription(String newName, String description, Long taskId);

    List<Task> findOnDeadlineDate(int pageNumber, int pageSize);

    Integer countOnTaskStatus(long perfId, TaskStatus status);
}
