package app.service.impl;

import app.dao.interfaces.IDepartmentDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.Department;
import app.service.interfaces.IDepartmentService;
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
