package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.CoreProperty;
import java.util.List;

public interface ICorePropertyDao extends IGenericDao<CoreProperty> {
    CoreProperty retrieveByName(String name);

    List<CoreProperty> findCached();
}
