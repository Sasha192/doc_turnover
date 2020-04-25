package app.service.impl;

import app.dao.ICorePropertyDao;
import app.dao.persistance.IGenericDao;
import app.models.CoreProperty;
import app.service.ICorePropertyService;
import app.service.abstraction.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CorePropertyService extends AbstractService<CoreProperty>
        implements ICorePropertyService {

    @Autowired
    private ICorePropertyDao dao;

    public CorePropertyService() {

    }

    @Override
    protected IGenericDao<CoreProperty> getDao() {
        return dao;
    }

    public void setDao(final ICorePropertyDao dao) {
        this.dao = dao;
    }

}
