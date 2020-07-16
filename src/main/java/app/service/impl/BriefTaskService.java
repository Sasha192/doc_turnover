package app.service.impl;

import app.dao.IBriefTaskDao;
import app.dao.persistance.IGenericDao;
import app.models.BriefTask;
import app.models.Performer;
import app.service.IBriefTaskService;
import app.service.abstraction.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BriefTaskService extends AbstractService<BriefTask>
        implements IBriefTaskService {

    private IBriefTaskDao dao;

    public BriefTaskService() {
        ;
    }

    @Override
    protected IGenericDao<BriefTask> getDao() {
        return dao;
    }

    @Autowired
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
