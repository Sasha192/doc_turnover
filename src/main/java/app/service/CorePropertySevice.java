package app.service;

import app.dao.CorePropertyDao;
import app.models.CoreProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CorePropertySevice {

    @Autowired
    private CorePropertyDao corePropertyDao;

    @Transactional
    public Integer insert(CoreProperty property) {
        return corePropertyDao.insert(property);
    }

    public CorePropertyDao getCorePropertyDao() {
        return corePropertyDao;
    }

    public void setCorePropertyDao(CorePropertyDao corePropertyDao) {
        this.corePropertyDao = corePropertyDao;
    }
}
