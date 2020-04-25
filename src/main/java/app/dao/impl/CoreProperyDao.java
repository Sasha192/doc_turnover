package app.dao.impl;

import app.dao.ICorePropertyDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.CoreProperty;
import org.springframework.stereotype.Repository;

@Repository
public class CoreProperyDao extends GenericJpaRepository<CoreProperty>
        implements ICorePropertyDao {

    public CoreProperyDao() {
        setClazz(CoreProperty.class);
    }

}
