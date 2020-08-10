package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.Department;
import java.util.List;

public interface IDepartmentService extends IOperations<Department> {

    Department retrieveOneByName(String name);

    List<Department> retrieveByParent(Long parentId);

    void changeDeparmentName(String newName, Long depId);

}
