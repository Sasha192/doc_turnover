package app.service.impl;

import app.dao.interfaces.ITaskCommentDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.TaskComment;
import app.models.basic.taskmodels.Task;
import app.service.abstraction.AbstractService;
import app.service.interfaces.ITaskCommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskCommentService
        extends AbstractService<TaskComment>
        implements ITaskCommentService {

    private ITaskCommentDao dao;

    @Autowired
    public TaskCommentService(ITaskCommentDao dao) {
        this.dao = dao;
    }

    @Override
    protected IGenericDao<TaskComment> getDao() {
        return dao;
    }

    public void setDao(final ITaskCommentDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskComment> retrieveByTask(Task task) {
        return dao.retrieveByTask(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskComment> retrieveByTaskName(String taskName) {
        return dao.retrieveByTaskName(taskName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskComment> retrieveByTaskId(Long id) {
        return dao.retrieveByTaskId(id);
    }
}
