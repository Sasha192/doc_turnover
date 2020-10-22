package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IPerformerDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IPerformerService;
import app.customtenant.service.interfaces.IPerformerUpdateEventListenerService;
import app.security.models.SimpleRole;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerformerService extends AbstractService<Performer>
        implements IPerformerService {

    @Autowired
    private IPerformerDao dao;

    @Autowired
    @Qualifier("perf_update_listener")
    private IPerformerUpdateEventListenerService listenerService;

    public PerformerService() {

    }

    @Override
    protected IGenericDao<Performer> getDao() {
        return dao;
    }

    public void setDao(final IPerformerDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Performer> findByDepartmentId(Long departmentId) {
        return dao.findByDepartmentId(departmentId);
    }

    @Override
    public Performer retrieveByUserId(Long id) {
        return dao.retrieveByUserId(id);
    }

    @Override
    public Performer update(Performer entity) {
        listenerService.setUpdate(entity.getId());
        return super.update(entity);
    }

    @Override
    public int updatePerformerDepartment(long perfId, long depoId) {
        listenerService.setUpdate(perfId);
        return dao.updatePerformerDepartment(perfId, depoId);
    }

    @Override
    public int updatePerformerRole(Long performerId, SimpleRole role) {
        listenerService.setUpdate(performerId);
        return dao.updatePerformerRole(performerId, role);
    }
}
