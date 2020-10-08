package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IDepartmentDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Department;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IDepartmentService;
import java.util.List;
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

    @Override
    public Department retrieveOneByName(String name) {
        return departmentDao.retrieveOneByName(name);
    }

    @Override
    public List<Department> retrieveByParent(Long parentId) {
        return departmentDao.retrieveByParent(parentId);
    }

    @Override
    public void changeDeparmentName(String newName, Long depId) {
        departmentDao.changeDeparmentName(newName, depId);
    }
}
