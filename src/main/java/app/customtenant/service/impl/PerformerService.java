package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IDepartmentDao;
import app.customtenant.dao.interfaces.IPerformerDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Department;
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

    private final IPerformerDao dao;

    private final IDepartmentDao departmentDao;

    private final IPerformerUpdateEventListenerService listenerService;

    public PerformerService(IPerformerDao dao,
                            IDepartmentDao departmentDao,
                            @Qualifier("perf_update_listener")
                                    IPerformerUpdateEventListenerService listenerService) {

        this.dao = dao;
        this.departmentDao = departmentDao;
        this.listenerService = listenerService;
    }

    @Override
    protected IGenericDao<Performer> getDao() {
        return dao;
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
        updated(entity.getId());
        return super.update(entity);
    }

    @Override
    public void updatePerformerDepartment(long performerId, long departmentId) {
        dao.updatePerformerDepartment(performerId, departmentId);
        departmentDao.incrementCounter(departmentId);
        updated(performerId);
    }

    @Override
    public void updatePerformerRole(Long performerId, SimpleRole role) {
        updated(performerId);
        dao.updatePerformerRole(performerId, role);
    }

    private void updated(Long performerId) {
        listenerService.setUpdate(performerId);
    }
}
