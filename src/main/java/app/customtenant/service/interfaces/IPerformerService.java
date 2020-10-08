package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.Performer;
import app.security.models.auth.CustomUser;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IPerformerService extends IOperations<Performer> {

    //Performer retrieveByName(String name);

    Performer retrieveByUserDetails(UserDetails userDetails);

    Performer retrieveByUser(CustomUser user);

    Performer retrieveByUsername(String username);

    List<Performer> findByDepartmentId(Long departmentId);
}
