package app.service.impl;

import app.dao.interfaces.IBriefPerformerDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.CustomUser;
import app.models.mysqlviews.BriefPerformer;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IBriefPerformerService;
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
