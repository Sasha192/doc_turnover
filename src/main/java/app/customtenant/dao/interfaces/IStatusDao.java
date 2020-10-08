package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.TaskStatus;
import java.util.List;

public interface IStatusDao extends IGenericDao<TaskStatus> {

    List<TaskStatus> findByPerformerId(long id);

    TaskStatus findByTitleAndPerformer(Long performerId, String title);

    TaskStatus findByTitle(String title);

    List<TaskStatus> findAllNotCustom();
}
