package app.service;

import app.dao.persistance.IOperations;
import app.models.Performer;
import app.models.Task;
import java.util.List;
import java.util.Map;

public interface ITaskService extends IOperations<Task> {

    List<Task> findBy(Map<String, String> filters);

    List<Task> findByPerformer(Performer performer);

    List<Task> findByPerformerIdStaticStatus(long id);
}
