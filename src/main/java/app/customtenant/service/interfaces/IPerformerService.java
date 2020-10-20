package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.Performer;
import java.util.List;

public interface IPerformerService extends IOperations<Performer> {

    List<Performer> findByDepartmentId(Long departmentId);

    Performer retrieveByUserId(Long id);
}
