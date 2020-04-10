package app.dao.impl;

import app.dao.persistance.AbstractHibernateDao;
import app.models.Department;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDao extends AbstractHibernateDao<Department> {

    public DepartmentDao() {
        setClazz(Department.class);
    }

}
