package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IBriefPerformerDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.mysqlviews.BriefPerformer;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IBriefPerformerService;
import app.security.models.auth.CustomUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BriefPerformerService
        extends AbstractService<BriefPerformer>
        implements IBriefPerformerService {

    @Autowired
    private IBriefPerformerDao dao;

    @Override
    protected IGenericDao<BriefPerformer> getDao() {
        return dao;
    }

    @Override
    public BriefPerformer retrieveByUserDetails(UserDetails userDetails) {
        return dao.retrieveByUserDetails(userDetails);
    }

    @Override
    public BriefPerformer retrieveByUser(CustomUser user) {
        return dao.retrieveByUser(user);
    }

    @Override
    public BriefPerformer retrieveByUsername(String username) {
        return dao.retrieveByUsername(username);
    }

    @Override
    public List<BriefPerformer> findByDepartmentId(Long departmentId) {
        return dao.findByDepartmentId(departmentId);
    }
}
