package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IBriefTaskDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.mysqlviews.BriefTask;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IBriefTaskService;
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

    @Override
    public List<BriefTask> findByDepartmentAndStatus(Long depoId, String status) {
        return dao.findByDepartmentAndStatus(depoId, status);
    }

    @Override
    public List<BriefTask> findByDepartment(Long depoId) {
        return dao.findByDepartment(depoId);
    }
}
