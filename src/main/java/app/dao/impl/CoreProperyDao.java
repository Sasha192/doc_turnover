package app.dao.impl;

import app.dao.ICorePropertyDao;
import app.dao.persistance.GenericJpaDao;
import app.models.CoreProperty;

import java.rmi.NoSuchObjectException;
import java.util.List;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CoreProperyDao extends GenericJpaDao<CoreProperty>
        implements ICorePropertyDao {

    public CoreProperyDao() {
        setClazz(CoreProperty.class);
    }

    @Override
    public CoreProperty retrieveByName(String name) throws NoSuchObjectException {
        String className = this.getClazz().getName();
        Query query = this.getEntityManager()
                .createQuery("FROM CoreProperty cp WHERE cp.name=:name");
        query.setParameter("name", name);
        List<CoreProperty> list = query.getResultList();
        if (list.isEmpty()) {
            throw new NoSuchObjectException("No such document exception");
        }
        return list.get(0);
    }

}
