package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.TaskStatus;
import java.util.List;

public interface IStatusService extends IOperations<TaskStatus> {

    List<TaskStatus> findByPerformerId(long id);

    TaskStatus findByTitleAndPerformer(Long performerId, String title);

    TaskStatus findByTitle(String title);

    List<TaskStatus> findAllNotCustom();
}
