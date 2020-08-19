package app.security.service.impl;

import app.dao.persistance.IGenericDao;
import app.models.VerificationCode;
import app.models.basic.CustomUser;
import app.security.dao.IUserDao;
import app.security.models.RememberMeToken;
import app.security.service.IUserService;
import app.service.abstraction.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends AbstractService<CustomUser>
        implements IUserService {

    @Autowired
    private IUserDao dao;

    public UserService() {
        ;
    }

    @Override
    protected IGenericDao<CustomUser> getDao() {
        return dao;
    }

    public void setDao(final IUserDao dao) {
        this.dao = dao;
    }

    @Override
    public CustomUser retrieveByName(String userName) {
        return dao.retrieveByEmail(userName);
    }

    @Override
    public VerificationCode retrieveVerificationCode(long id) {
        return dao.retrieveVerificationCode(id);
    }

    @Override
    public void registerVerificationCode(VerificationCode code) {
        dao.registerVerificationCode(code);
    }

    @Override
    public void removeVerificationCode(long id) {
        dao.removeVerificationCode(id);
    }

    @Override
    public RememberMeToken retrieveRememberMeToken(long id) {
        return dao.retrieveRememberMeToken(id);
    }

    @Override
    public void registerRememberMeToken(RememberMeToken token) {
        dao.registerRememberMeToken(token);
    }

    @Override
    public void removeRememberMeToken(long id) {
        dao.removeRememberMeToken(id);
    }
}
