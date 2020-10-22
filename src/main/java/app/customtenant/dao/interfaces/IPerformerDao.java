package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Performer;
import app.security.models.SimpleRole;
import java.util.List;

public interface IPerformerDao extends IGenericDao<Performer> {

    List<Performer> findByDepartmentId(Long departmentId);

    Performer retrieveByUserId(Long id);

    int updatePerformerDepartment(long perfId, long depoId);

    int updatePerformerRole(Long performerId, SimpleRole role);

    //Performer retrieveByName(String name) throws NoSuchObjectException;

}
