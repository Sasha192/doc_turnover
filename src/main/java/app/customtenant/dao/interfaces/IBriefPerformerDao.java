package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.mysqlviews.BriefPerformer;
import app.security.models.auth.CustomUser;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IBriefPerformerDao extends IGenericDao<BriefPerformer> {

    BriefPerformer retrieveByUserDetails(UserDetails userDetails);

    BriefPerformer retrieveByUser(CustomUser user);

    BriefPerformer retrieveByUsername(String username);

    List<BriefPerformer> findByDepartmentId(Long departmentId);

}
