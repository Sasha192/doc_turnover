package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Performer;
import app.security.models.auth.CustomUser;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IPerformerDao extends IGenericDao<Performer> {
    Performer retrieveByUserDetails(UserDetails userDetails);

    Performer retrieveByUser(CustomUser user);

    Performer retrieveByUsername(String username);

    List<Performer> findByDepartmentId(Long departmentId);

    //Performer retrieveByName(String name) throws NoSuchObjectException;

}
