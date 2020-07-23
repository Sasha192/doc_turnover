package app.security.dao.impl;

import app.dao.persistance.GenericJpaRepository;
import app.models.CustomUser;
import app.models.VerificationCode;
import app.security.dao.IUserDao;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenericJpaRepository<CustomUser> implements IUserDao {

    private static final String SELECT_BY_USERNAME =
            "from CustomUser where email=:email";

    public UserDao() {
        setClazz(CustomUser.class);
    }

    public CustomUser retrieveByEmail(String email) {
        TypedQuery<CustomUser> query = getEntityManager()
                .createQuery(SELECT_BY_USERNAME, CustomUser.class);
        query.setParameter("email", email);
        List<CustomUser> users = query.getResultList();
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public VerificationCode retrieveVerificationCode(long id) {
        return getEntityManager().find(VerificationCode.class, id);
    }

    @Override
    public void registerVerificationCode(VerificationCode code) {
        getEntityManager().persist(code);
    }
}
