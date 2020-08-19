package app.security.dao;

import app.dao.persistance.IGenericDao;
import app.models.VerificationCode;
import app.models.basic.CustomUser;
import app.security.models.RememberMeToken;

public interface IUserDao extends IGenericDao<CustomUser> {

    CustomUser retrieveByEmail(String email);

    VerificationCode retrieveVerificationCode(long id);

    void registerVerificationCode(VerificationCode code);

    void removeVerificationCode(long id);

    RememberMeToken retrieveRememberMeToken(long id);

    void registerRememberMeToken(RememberMeToken token);

    void removeRememberMeToken(long id);
}
