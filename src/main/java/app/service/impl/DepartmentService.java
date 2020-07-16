package app.service.impl;

import app.dao.IDepartmentDao;
import app.dao.persistance.IGenericDao;
import app.models.Department;
import app.service.IDepartmentService;
import app.service.abstraction.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends AbstractService<Department>
        implements IDepartmentService {

    @Autowired
    private IDepartmentDao departmentDao;

    public DepartmentService() {
        ;
    }

    public void setDao(final IDepartmentDao dao) {
        this.departmentDao = dao;
    }

    @Override
    protected IGenericDao<Department> getDao() {
        return departmentDao;
    }
}
