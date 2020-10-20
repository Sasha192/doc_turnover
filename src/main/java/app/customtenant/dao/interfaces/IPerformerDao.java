package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Performer;
import app.security.models.auth.CustomUser;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IPerformerDao extends IGenericDao<Performer> {

    List<Performer> findByDepartmentId(Long departmentId);

    Performer retrieveByUserId(Long id);

    //Performer retrieveByName(String name) throws NoSuchObjectException;

}
