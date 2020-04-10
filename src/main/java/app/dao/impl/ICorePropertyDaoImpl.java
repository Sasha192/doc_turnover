package app.dao.impl;

import app.dao.ICorePropertyDao;
import app.models.CoreProperty;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ICorePropertyDaoImpl implements ICorePropertyDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Integer insert(CoreProperty property) {
        return (Integer) sessionFactory.getCurrentSession().save(property);
    }

    @Override
    public CoreProperty findByName(String name) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CoreProperty.class);
        return (CoreProperty) criteria.add(Restrictions.eq("name", name))
                .uniqueResult();
    }
}
