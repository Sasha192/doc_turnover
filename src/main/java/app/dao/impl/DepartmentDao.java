package app.dao.impl;

import app.dao.interfaces.IDepartmentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.Department;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDao extends GenericJpaRepository<Department>
        implements IDepartmentDao {

    public DepartmentDao() {
        setClazz(Department.class);
    }

}
