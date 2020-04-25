package app.dao.impl;

import app.dao.IPerformerDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.Performer;
import org.springframework.stereotype.Repository;

@Repository
public class PerformerDao extends GenericJpaRepository<Performer>
        implements IPerformerDao {

    public PerformerDao() {
        setClazz(Performer.class);
    }

}
