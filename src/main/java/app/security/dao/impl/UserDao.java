package app.security.dao.impl;

import app.dao.persistance.GenericJpaRepository;
import app.models.VerificationCode;
import app.models.basic.CustomUser;
import app.security.dao.IUserDao;
import app.security.models.RememberMeToken;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenericJpaRepository<CustomUser> implements IUserDao {

    private static final String SELECT_BY_USERNAME =
            "SELECT cu FROM CustomUser cu where email=:email";

    private static final String REMOVE_CODE =
            "DELETE FROM VerificationCode WHERE id=:id_";

    private static final String REMOVE_TOKEN =
            "DELETE FROM RememberMeToken WHERE id=:id_";

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

    @Override
    public void removeVerificationCode(long id) {
        getEntityManager().createQuery(REMOVE_CODE)
                .setParameter("id_", id)
                .executeUpdate();
    }

    @Override
    public RememberMeToken retrieveRememberMeToken(long id) {
        return getEntityManager().find(RememberMeToken.class, id);
    }

    @Override
    public void registerRememberMeToken(RememberMeToken token) {
        getEntityManager().persist(token);
    }

    @Override
    public void removeRememberMeToken(long id) {
        getEntityManager().createQuery(REMOVE_TOKEN)
                .setParameter("id_", id)
                .executeUpdate();
    }
}
