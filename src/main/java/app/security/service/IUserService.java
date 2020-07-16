package app.security.service;

import app.dao.persistance.IOperations;
import app.models.CustomUser;
import app.models.VerificationCode;

public interface IUserService extends IOperations<CustomUser> {

    CustomUser retrieveByName(String userName);

    VerificationCode retrieveVerificationCode(long id);

    void registerVerificationCode(VerificationCode code);

    void removeVerificationCode(long id);
}
