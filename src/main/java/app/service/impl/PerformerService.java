package app.service.impl;

import app.dao.IPerformerDao;
import app.dao.persistance.IGenericDao;
import app.models.Performer;
import app.service.IPerformerService;
import app.service.abstraction.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformerService extends AbstractService<Performer>
        implements IPerformerService {

    @Autowired
    private IPerformerDao dao;

    public PerformerService() {

    }

    @Override
    protected IGenericDao<Performer> getDao() {
        return dao;
    }

    public void setDao(final IPerformerDao dao) {
        this.dao = dao;
    }
}
