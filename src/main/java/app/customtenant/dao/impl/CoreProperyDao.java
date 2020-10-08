package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.ICorePropertyDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.basic.CoreProperty;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class CoreProperyDao extends GenericJpaRepository<CoreProperty>
        implements ICorePropertyDao {

    private static final String FROM =
            "SELECT corep FROM " + CoreProperty.class.getName() + " corep ";
    private static final String RETRIEVE_BY_NAME = FROM
            .concat(" WHERE corep.name=:name ");

    public CoreProperyDao() {
        setClazz(CoreProperty.class);
    }

    @Override
    public CoreProperty retrieveByName(String name) {
        TypedQuery<CoreProperty> query = getEntityManager()
                .createQuery(RETRIEVE_BY_NAME, CoreProperty.class);
        query.setParameter("name", name);
        List<CoreProperty> list = query.getResultList();
        if (list != null && (!list.isEmpty())) {
            return list.get(0);
        }
        return null;
    }
}
