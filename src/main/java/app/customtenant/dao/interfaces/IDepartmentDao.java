package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Department;
import java.util.List;

public interface IDepartmentDao extends IGenericDao<Department> {
    Department retrieveOneByName(String name);

    List<Department> retrieveByParent(Long parentId);

    void changeDeparmentName(String newName, Long depId);
    //

}
