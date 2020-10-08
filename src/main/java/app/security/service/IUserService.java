package app.security.service;

import app.customtenant.dao.persistance.IOperations;
import app.security.models.auth.CustomUser;
import app.security.models.auth.RememberMeToken;
import app.security.models.auth.UserInfo;

public interface IUserService extends IOperations<CustomUser> {

    UserInfo saveUserInfo(UserInfo info);

    CustomUser retrieveByName(String userName);

    RememberMeToken retrieveRememberMeToken(long id);

    void registerRememberMeToken(RememberMeToken token);

    void removeRememberMeToken(long id);
}
