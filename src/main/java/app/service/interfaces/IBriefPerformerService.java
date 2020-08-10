package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.CustomUser;
import app.models.mysqlviews.BriefPerformer;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IBriefPerformerService extends IOperations<BriefPerformer> {

    BriefPerformer retrieveByUserDetails(UserDetails userDetails);

    BriefPerformer retrieveByUser(CustomUser user);

    BriefPerformer retrieveByUsername(String username);

    List<BriefPerformer> findByDepartmentId(Long departmentId);

}
