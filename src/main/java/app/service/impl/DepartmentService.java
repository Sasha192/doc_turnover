package app.service.impl;

import app.dao.interfaces.IDepartmentDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.Department;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
