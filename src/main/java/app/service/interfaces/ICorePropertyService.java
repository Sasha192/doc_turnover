package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.CoreProperty;
import java.util.List;

public interface ICorePropertyService extends IOperations<CoreProperty> {

    CoreProperty retrieveByName(String name);

    List<CoreProperty> findCached();
}
