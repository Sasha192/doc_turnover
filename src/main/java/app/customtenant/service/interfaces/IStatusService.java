package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.TaskStatus;
import java.util.List;

public interface IStatusService extends IOperations<TaskStatus> {

    List<TaskStatus> findByPerformerId(long id);

    TaskStatus findByTitleAndPerformer(Long performerId, String title);

    TaskStatus findByTitle(String title);

    List<TaskStatus> findAllNotCustom();
}
