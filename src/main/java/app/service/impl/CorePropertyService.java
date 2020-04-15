package app.service.impl;

import app.dao.ICorePropertyDao;
import app.dao.persistance.IGenericDao;
import app.models.CoreProperty;
import app.service.ICorePropertyService;
import app.service.abstraction.AbstractService;

import java.rmi.NoSuchObjectException;

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

    @Override
    @Transactional(readOnly = true)
    public CoreProperty retrieveByName(final String name) {
        try {
            return dao.retrieveByName(name);
        } catch (NoSuchObjectException e) {
            return null;
        }
    }

    public void setDao(final ICorePropertyDao dao) {
        this.dao = dao;
    }

}
