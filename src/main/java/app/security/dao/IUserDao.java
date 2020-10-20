package app.security.dao;

import app.customtenant.dao.persistance.IGenericDao;
import app.security.models.auth.CustomUser;
import app.security.models.auth.RememberMeToken;
import app.security.models.auth.UserInfo;

public interface IUserDao extends IGenericDao<CustomUser> {

    CustomUser retrieveByEmail(String login);

    RememberMeToken retrieveRememberMeToken(long id);

    void registerRememberMeToken(RememberMeToken token);

    void removeRememberMeToken(long id);

    UserInfo saveUserInfo(UserInfo info);
}
