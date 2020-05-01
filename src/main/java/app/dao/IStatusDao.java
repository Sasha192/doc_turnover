package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.Status;

import java.util.List;

public interface IStatusDao extends IGenericDao<Status> {

    List<Status> findByPerformerId(Integer id);

}
