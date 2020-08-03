package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.CustomUser;
import app.models.basic.Performer;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IPerformerDao extends IGenericDao<Performer> {
    Performer retrieveByUserDetails(UserDetails userDetails);

    Performer retrieveByUser(CustomUser user);

    Performer retrieveByUsername(String username);

    List<Performer> findByDepartmentId(Long departmentId);

    //Performer retrieveByName(String name) throws NoSuchObjectException;

}
