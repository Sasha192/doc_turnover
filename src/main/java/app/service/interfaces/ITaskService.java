package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.Performer;
import app.models.basic.TaskStatus;
import app.models.basic.taskmodels.Task;
import java.util.List;
import java.util.Map;

public interface ITaskService extends IOperations<Task> {

    List<Task> findBy(Map<String, String> filters);

    List<Task> findByPerformer(Performer performer);

    List<Task> findByPerformerIdStaticStatus(long id);

    void updateNameDescription(String newName,
                               String description,
                               Long taskId);

    List<Task> findOnDeadlineDate(int pageNumber, int pageSize);

    int countOnTaskStatus(long perfId, TaskStatus status);
}
