package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.TaskStatus;
import java.util.List;

public interface IStatusDao extends IGenericDao<TaskStatus> {

    List<TaskStatus> findByPerformerId(long id);

}
