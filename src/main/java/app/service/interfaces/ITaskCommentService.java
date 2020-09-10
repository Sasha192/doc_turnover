package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.TaskComment;
import app.models.basic.taskmodels.Task;
import java.util.List;

public interface ITaskCommentService extends IOperations<TaskComment> {

    List<TaskComment> retrieveByTask(Task task);

    List<TaskComment> retrieveByTaskName(String taskName);

    List<TaskComment> retrieveByTaskId(Long id);

}
