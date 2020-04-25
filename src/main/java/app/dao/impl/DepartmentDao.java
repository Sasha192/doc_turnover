package app.dao.impl;

import app.dao.IDepartmentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.Department;

public class DepartmentDao extends GenericJpaRepository<Department>
        implements IDepartmentDao {

    public DepartmentDao() {
        setClazz(Department.class);
    }

}
