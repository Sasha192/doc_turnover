package app.dao.impl;

import app.dao.CorePropertyDao;
import app.models.CoreProperty;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CorePropertyDaoImpl implements CorePropertyDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Integer insert(CoreProperty property) {
        return (Integer) sessionFactory.getCurrentSession().save(property);
    }
}
