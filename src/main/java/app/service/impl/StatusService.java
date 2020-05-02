package app.service.impl;

import app.dao.IStatusDao;
import app.dao.persistance.IGenericDao;
import app.models.TaskStatus;
import app.service.IStatusService;
import app.service.abstraction.AbstractService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<TaskStatus> findByPerformerId(int id) {
        return dao.findByPerformerId(id);
    }
}
