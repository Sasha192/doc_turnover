package app.security.service.impl;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.service.abstraction.AbstractService;
import app.security.dao.IUserDao;
import app.security.models.auth.CustomUser;
import app.security.models.auth.RememberMeToken;
import app.security.models.auth.UserInfo;
import app.security.service.IUserService;
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

    @Override
    public void updateUserInfo(UserInfo info) {
        dao.updateUserInfo(info);
    }

    @Override
    public UserInfo saveUserInfo(UserInfo info) {
        return dao.saveUserInfo(info);
    }
}
