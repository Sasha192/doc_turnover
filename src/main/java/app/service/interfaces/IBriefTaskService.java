package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.Performer;
import app.models.mysqlviews.BriefTask;
import java.util.List;

public interface IBriefTaskService extends IOperations<BriefTask> {

    List<BriefTask> findByPerformer(Long performerId);

    List<BriefTask> findByPerformer(Performer performerId);

    List<BriefTask> findByPerformerAndStatus(Long performerId, String status);

    List<BriefTask> findByStatus(String status);
}
