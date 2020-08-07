package app.service.impl;

import app.dao.interfaces.IStatusDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.TaskStatus;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IStatusService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatusService extends AbstractService<TaskStatus>
        implements IStatusService {

    @Autowired
    private IStatusDao dao;

    public StatusService() {

    }

    @Override
    protected IGenericDao<TaskStatus> getDao() {
        return dao;
    }

    public void setDao(final IStatusDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatus> findByPerformerId(long id) {
        return dao.findByPerformerId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatus findByTitleAndPerformer(Long performerId, String title) {
        return dao.findByTitleAndPerformer(performerId, title);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatus findByTitle(String title) {
        return dao.findByTitle(title);
    }
}
