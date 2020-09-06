package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.TaskStatus;
import java.util.List;

public interface IStatusDao extends IGenericDao<TaskStatus> {

    List<TaskStatus> findByPerformerId(long id);

    TaskStatus findByTitleAndPerformer(Long performerId, String title);

    TaskStatus findByTitle(String title);

    List<TaskStatus> findAllNotCustom();
}
