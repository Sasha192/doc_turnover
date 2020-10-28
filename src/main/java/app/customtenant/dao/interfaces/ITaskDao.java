package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import java.util.Date;
import java.util.List;

public interface ITaskDao extends IGenericDao<Task> {

    List<Task> findByPerformerId(int page, int pageSize, long perfId);

    void updateNameDescription(String newName, String description, Long taskId);

    List<Task> findOnDeadlineDate(int pageNumber, int pageSize, Date deadlineDate);

    List<Task> findByDepartment(int pageId, int pageSize, long departmentId);

    List<Task> findByStatus(int page, int pageSize, TaskStatus byName);

    List<Task> findByPerformerAndStatus(int pageId, int pageSize,
                                        long performerId, TaskStatus byName);

    List<Task> findOnControlDate(int page, int pageSize, Date controlDate);
}
