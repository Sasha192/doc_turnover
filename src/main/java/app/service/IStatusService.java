package app.service;

import app.dao.persistance.IOperations;
import app.models.Status;

import java.util.List;

public interface IStatusService extends IOperations<Status> {

    List<Status> findByPerformerId(int id);

}
