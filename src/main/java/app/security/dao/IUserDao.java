package app.security.dao;

import app.dao.persistance.IGenericDao;
import app.models.VerificationCode;
import app.models.basic.CustomUser;

public interface IUserDao extends IGenericDao<CustomUser> {

    CustomUser retrieveByEmail(String email);

    VerificationCode retrieveVerificationCode(long id);

    void registerVerificationCode(VerificationCode code);
}
