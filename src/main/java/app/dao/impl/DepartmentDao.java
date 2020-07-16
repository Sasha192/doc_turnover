package app.dao.impl;

import app.dao.IDepartmentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.Department;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDao extends GenericJpaRepository<Department>
        implements IDepartmentDao {

    public DepartmentDao() {
        setClazz(Department.class);
    }

}
