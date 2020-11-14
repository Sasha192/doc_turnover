package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.ITaskDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.eventdriven.publishers.TaskEventPublisher;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.ITaskService;
import app.tenantconfiguration.TenantContext;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService extends AbstractService<Task>
        implements ITaskService {

    @Autowired
    private ITaskDao dao;

    @Autowired
    private TaskEventPublisher publisher;

    @Override
    protected IGenericDao<Task> getDao() {
        return dao;
    }

    public void setDao(final ITaskDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByPerformer(int page, int pageSize, long perfId) {
        return dao.findByPerformerId(page, pageSize, perfId);
    }

    @Override
    public void updateNameDescription(String newName,
                                      String description,
                                      Long taskId) {
        dao.updateNameDescription(newName, description, taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findOnControlDate(int page, int pageSize, Date controlDate) {
        return dao.findOnControlDate(page, pageSize, controlDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findOnDeadlineDate(int pageNumber, int pageSize,
                                                     Date deadLineDate) {
        return dao.findOnDeadlineDate(pageNumber, pageSize, deadLineDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByDepartment(int pageId, int pageSize, long departmentId) {
        return dao.findByDepartment(pageId, pageSize, departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByStatus(
            int page, int pageSize, TaskStatus byName) {
        return dao.findByStatus(page, pageSize, byName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByPerformerAndStatus(
            int pageId, int pageSize, long performerId, TaskStatus byName) {
        return dao.findByPerformerAndStatus(pageId, pageSize, performerId, byName);
    }
}

