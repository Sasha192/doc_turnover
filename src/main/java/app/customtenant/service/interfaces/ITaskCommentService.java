package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.TaskComment;
import app.customtenant.models.basic.taskmodels.Task;
import java.util.List;

public interface ITaskCommentService extends IOperations<TaskComment> {

    List<TaskComment> retrieveByTask(Task task);

    List<TaskComment> retrieveByTaskName(String taskName);

    List<TaskComment> retrieveByTaskId(Long id);

}
