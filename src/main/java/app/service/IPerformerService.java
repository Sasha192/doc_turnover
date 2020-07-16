package app.service;

import app.dao.persistance.IOperations;
import app.models.CustomUser;
import app.models.Performer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IPerformerService extends IOperations<Performer> {

    //Performer retrieveByName(String name);

    Performer retrieveByUserDetails(UserDetails userDetails);

    Performer retrieveByUser(CustomUser user);

    Performer retrieveByUsername(String username);

    List<Performer> findByDepartmentId(Long departmentId);

}
