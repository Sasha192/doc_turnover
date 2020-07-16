package app.security.dao;

import app.dao.persistance.IGenericDao;
import app.models.CustomUser;
import app.models.VerificationCode;

public interface IUserDao extends IGenericDao<CustomUser> {

    CustomUser retrieveByEmail(String email);

    VerificationCode retrieveVerificationCode(long id);

    void registerVerificationCode(VerificationCode code);
}
