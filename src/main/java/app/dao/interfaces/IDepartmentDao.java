package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.Department;
import java.util.List;

public interface IDepartmentDao extends IGenericDao<Department> {
    Department retrieveOneByName(String name);

    List<Department> retrieveByParent(Long parentId);

    void changeDeparmentName(String newName, Long depId);
    //

}
