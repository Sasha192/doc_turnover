package app.dao.impl;

import app.dao.persistance.AbstractHibernateDao;
import app.models.Performer;
import org.springframework.stereotype.Repository;

@Repository
public class PerformerDao extends AbstractHibernateDao<Performer> {

    public PerformerDao() {
        setClazz(Performer.class);
    }

}
