package app.service.impl;

import app.dao.interfaces.IBriefTaskDao;
import app.dao.persistance.IGenericDao;
import app.models.mysqlviews.BriefTask;
import app.models.basic.Performer;
import app.service.interfaces.IBriefTaskService;
import app.service.abstraction.AbstractService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BriefTaskService extends AbstractService<BriefTask>
        implements IBriefTaskService {

    private IBriefTaskDao dao;

    @Autowired
    public BriefTaskService(IBriefTaskDao dao) {
        this.dao = dao;
    }

    @Override
    protected IGenericDao<BriefTask> getDao() {
        return dao;
    }

    public void setDao(final IBriefTaskDao dao) {
        this.dao = dao;
    }

    @Override
    public List<BriefTask> findByPerformer(Long performerId) {
        return dao.findByPerformer(performerId);
    }

    @Override
    public List<BriefTask> findByPerformer(Performer performer) {
        return dao.findByPerformer(performer);
    }

    @Override
    public List<BriefTask> findByPerformerAndStatus(Long performerId, String status) {
        return dao.findByPerformerAndStatus(performerId, status);
    }

    @Override
    public List<BriefTask> findByStatus(String status) {
        return dao.findByStatus(status);
    }
}
