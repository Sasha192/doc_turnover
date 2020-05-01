package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.Task;

import java.util.List;

public interface ITaskDao extends IGenericDao<Task> {

    List<Task> findByPerformerId(int id);

    List<Task> findByPerformerIdStaticStatus(int id);

}
