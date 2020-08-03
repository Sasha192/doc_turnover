package app.service.impl;

import app.dao.interfaces.IPerformerDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.CustomUser;
import app.models.basic.Performer;
import app.service.interfaces.IPerformerService;
import app.service.abstraction.AbstractService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PerformerService extends AbstractService<Performer>
        implements IPerformerService {

    @Autowired
    private IPerformerDao dao;

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
    public Performer retrieveByUserDetails(UserDetails userDetails) {
        return dao.retrieveByUserDetails(userDetails);
    }

    @Override
    public Performer retrieveByUser(CustomUser user) {
        return dao.retrieveByUser(user);
    }

    @Override
    public Performer retrieveByUsername(String username) {
        return dao.retrieveByUsername(username);
    }

    @Override
    public List<Performer> findByDepartmentId(Long departmentId) {
        return dao.findByDepartmentId(departmentId);
    }
}
