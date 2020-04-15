package app.dao.impl;

import app.dao.IPerformerDao;
import app.dao.persistance.GenericJpaDao;
import app.models.Performer;

public class PerformerDao extends GenericJpaDao<Performer>
        implements IPerformerDao {

    public PerformerDao() {
        setClazz(Performer.class);
    }

}
