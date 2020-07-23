package app.service;

import app.dao.persistance.IOperations;
import app.models.CustomUser;
import app.models.Performer;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IPerformerService extends IOperations<Performer> {

    //Performer retrieveByName(String name);

    Performer retrieveByUserDetails(UserDetails userDetails);

    Performer retrieveByUser(CustomUser user);

    Performer retrieveByUsername(String username);

    List<Performer> findByDepartmentId(Long departmentId);

}
