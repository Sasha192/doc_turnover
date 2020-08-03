package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.basic.Task;
import java.util.List;

public interface ITaskDao extends IGenericDao<Task> {

    List<Task> findByPerformerId(long id);

    List<Task> findByPerformerIdStaticStatus(long id);

}
