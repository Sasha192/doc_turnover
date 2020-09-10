package app.service.impl;

import app.dao.interfaces.ITaskDao;
import app.dao.persistance.IGenericDao;
import app.eventdriven.publishers.TaskEventPublisher;
import app.models.basic.Performer;
import app.models.basic.TaskStatus;
import app.models.basic.taskmodels.Task;
import app.service.abstraction.AbstractService;
import app.service.interfaces.ITaskService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService extends AbstractService<Task>
        implements ITaskService {

    @Autowired
    private ITaskDao dao;

    @Autowired
    private TaskEventPublisher publisher;

    public TaskService() {
    }

    @Override
    protected IGenericDao<Task> getDao() {
        return dao;
    }

    public void setDao(final ITaskDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findBy(Map<String, String> filters) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByPerformer(Performer performer) {
        return dao.findByPerformerId(performer.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByPerformerIdStaticStatus(long id) {
        return dao.findByPerformerIdStaticStatus(id);
    }

    @Override
    public void updateNameDescription(String newName,
                                      String description,
                                      Long taskId) {
        dao.updateNameDescription(newName, description, taskId);
    }

    @Override
    public Task create(Task entity) {
        Task task = super.create(entity);
        publisher.onCreate(task);
        return task;
    }

    @Override
    public void create(List<Task> entities) {
        super.create(entities);
    }

    @Override
    public List<Task> findOnDeadlineDate(int pageNumber, int pageSize) {
        return dao.findOnDeadlineDate(pageNumber, pageSize);
    }

    @Override
    public int countOnTaskStatus(long perfId, TaskStatus status) {
        return dao.countOnTaskStatus(perfId, status);
    }
}

