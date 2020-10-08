package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.mysqlviews.BriefTask;
import java.util.List;

public interface IBriefTaskService extends IOperations<BriefTask> {

    List<BriefTask> findByPerformer(Long performerId);

    List<BriefTask> findByPerformer(Performer performerId);

    List<BriefTask> findByPerformerAndStatus(Long performerId, String status);

    List<BriefTask> findByStatus(String status);

    List<BriefTask> findByDepartment(Long depoId);

    List<BriefTask> findByDepartmentAndStatus(Long depoId, String status);
}
