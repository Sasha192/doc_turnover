package app.dao.impl;

import app.dao.IDepartmentDao;
import app.dao.persistance.GenericJpaDao;
import app.models.Department;

public class DepartmentDao extends GenericJpaDao<Department>
        implements IDepartmentDao {

    public DepartmentDao() {
        setClazz(Department.class);
    }

}
