package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.CustomUser;
import app.models.Performer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IPerformerDao extends IGenericDao<Performer> {
    Performer retrieveByUserDetails(UserDetails userDetails);

    Performer retrieveByUser(CustomUser user);

    Performer retrieveByUsername(String username);

    List<Performer> findByDepartmentId(Long departmentId);

    //Performer retrieveByName(String name) throws NoSuchObjectException;

}
