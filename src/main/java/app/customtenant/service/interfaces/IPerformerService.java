package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.Performer;
import app.security.models.SimpleRole;
import java.util.List;

public interface IPerformerService extends IOperations<Performer> {

    List<Performer> findByDepartmentId(Long departmentId);

    Performer retrieveByUserId(Long id);

    void updatePerformerDepartment(long perfId, long depoId);

    void updatePerformerRole(Long performerId, SimpleRole role);
}
