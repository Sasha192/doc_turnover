package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.Performer;
import app.models.mysqlviews.BriefTask;
import java.util.List;

public interface IBriefTaskDao extends IGenericDao<BriefTask> {
    List<BriefTask> findByPerformer(Long performerId);

    List<BriefTask> findByPerformer(Performer performer);

    List<BriefTask> findByPerformerAndStatus(Long performerId, String status);

    List<BriefTask> findByStatus(String status);

    List<BriefTask> findByDepartment(Long depoId);

    List<BriefTask> findByDepartmentAndStatus(Long depoId, String status);
}
