package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import java.util.List;

public interface ITaskService extends IOperations<Task> {

    List<Task> findByPerformer(int page, int pageSize, long perfId);

    List<Task> findOnDeadlineDate(int pageNumber, int pageSize);
    
    List<Task> findByDepartment(int pageId, int pageSize, long departmentId);
    
    void updateNameDescription(String newName,
                               String description,
                               Long taskId);

    List<Task> findByStatus(int page, int pageSize, TaskStatus byName);

    List<Task> findByPerformerAndStatus(
            int pageId, int pageSizeLong, long performerId, TaskStatus byName
    );
}
