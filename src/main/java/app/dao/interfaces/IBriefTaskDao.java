package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.mysqlviews.BriefTask;
import app.models.basic.Performer;
import java.util.List;

public interface IBriefTaskDao extends IGenericDao<BriefTask> {
    List<BriefTask> findByPerformer(Long performerId);

    List<BriefTask> findByPerformer(Performer performer);

    List<BriefTask> findByPerformerAndStatus(Long performerId, String status);

    List<BriefTask> findByStatus(String status);
}
