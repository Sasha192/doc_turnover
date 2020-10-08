package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IStatusDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IStatusService;
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

    @Override
    public List<TaskStatus> findAllNotCustom() {
        return dao.findAllNotCustom();
    }
}
