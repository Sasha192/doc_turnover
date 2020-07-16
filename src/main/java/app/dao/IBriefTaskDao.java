package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.BriefTask;
import app.models.Performer;

import java.util.List;

public interface IBriefTaskDao extends IGenericDao<BriefTask> {
    List<BriefTask> findByPerformer(Long performerId);

    List<BriefTask> findByPerformer(Performer performer);

    List<BriefTask> findByPerformerAndStatus(Long performerId, String status);

    List<BriefTask> findByStatus(String status);
}
