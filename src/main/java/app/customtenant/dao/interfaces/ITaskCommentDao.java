package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.TaskComment;
import app.customtenant.models.basic.taskmodels.Task;
import java.util.List;

public interface ITaskCommentDao extends IGenericDao<TaskComment> {

    List<TaskComment> retrieveByTask(Task task);

    List<TaskComment> retrieveByTaskName(String taskName);

    List<TaskComment> retrieveByTaskId(Long id);
}
