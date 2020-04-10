package app.service;

import app.dao.ICorePropertyDao;
import app.models.CoreProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CorePropertySevice {

    @Autowired
    private ICorePropertyDao ICorePropertyDao;

    @Transactional
    public Integer insert(CoreProperty property) {
        return ICorePropertyDao.insert(property);
    }

    @Transactional
    public CoreProperty findByName(String name) {
        return ICorePropertyDao.findByName(name);
    }

    public ICorePropertyDao getICorePropertyDao() {
        return ICorePropertyDao;
    }

    public void setICorePropertyDao(ICorePropertyDao ICorePropertyDao) {
        this.ICorePropertyDao = ICorePropertyDao;
    }
}
