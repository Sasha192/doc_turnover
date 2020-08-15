package app.service.impl;

import app.dao.interfaces.IPerformerDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.CustomUser;
import app.models.basic.Performer;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IPerformerService;
import app.service.interfaces.IPerformerUpdateEventListenerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Performer retrieveByUserDetails(UserDetails userDetails) {
        return dao.retrieveByUserDetails(userDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Performer retrieveByUser(CustomUser user) {
        return dao.retrieveByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Performer retrieveByUsername(String username) {
        return dao.retrieveByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Performer> findByDepartmentId(Long departmentId) {
        return dao.findByDepartmentId(departmentId);
    }

    @Override
    public Performer update(Performer entity) {
        listenerService.setUpdate(entity.getId());
        return super.update(entity);
    }
}
