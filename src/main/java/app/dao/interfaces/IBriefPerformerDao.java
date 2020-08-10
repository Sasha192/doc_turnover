package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.CustomUser;
import app.models.mysqlviews.BriefPerformer;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface IBriefPerformerDao extends IGenericDao<BriefPerformer> {

    BriefPerformer retrieveByUserDetails(UserDetails userDetails);

    BriefPerformer retrieveByUser(CustomUser user);

    BriefPerformer retrieveByUsername(String username);

    List<BriefPerformer> findByDepartmentId(Long departmentId);

}
