package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.mysqlviews.BriefPerformer;
import app.security.models.auth.CustomUser;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IBriefPerformerService extends IOperations<BriefPerformer> {

    BriefPerformer retrieveByUserDetails(UserDetails userDetails);

    BriefPerformer retrieveByUser(CustomUser user);

    BriefPerformer retrieveByUsername(String username);

    List<BriefPerformer> findByDepartmentId(Long departmentId);

}
