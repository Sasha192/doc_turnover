package app.service.impl;

import app.dao.interfaces.ICorePropertyDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.CoreProperty;
import app.service.abstraction.AbstractService;
import app.service.interfaces.ICorePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<CoreProperty> findCached() {
        return dao.findCached();
    }
}
