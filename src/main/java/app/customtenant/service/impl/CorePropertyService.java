package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.ICorePropertyDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.CoreProperty;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.ICorePropertyService;
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

    @Override
    @Transactional(readOnly = true)
    public CoreProperty retrieveByName(String name) {
        return dao.retrieveByName(name);
    }
}
