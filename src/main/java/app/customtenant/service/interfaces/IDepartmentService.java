package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.Department;
import java.util.List;

public interface IDepartmentService extends IOperations<Department> {

    Department retrieveOneByName(String name);

    List<Department> retrieveByParent(Long parentId);

    void changeDeparmentName(String newName, Long depId);

}
