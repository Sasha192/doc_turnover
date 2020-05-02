package app.service;

import app.dao.persistance.IOperations;
import app.models.TaskStatus;
import java.util.List;

public interface IStatusService extends IOperations<TaskStatus> {

    List<TaskStatus> findByPerformerId(int id);

}
