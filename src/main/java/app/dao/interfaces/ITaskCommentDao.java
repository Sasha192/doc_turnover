package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.Task;
import app.models.basic.TaskComment;

import java.util.List;

public interface ITaskCommentDao extends IGenericDao<TaskComment> {

    List<TaskComment> retrieveByTask(Task task);

    List<TaskComment> retrieveByTaskName(String taskName);

    List<TaskComment> retrieveByTaskId(Long id);
}
