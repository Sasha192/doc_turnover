package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.CoreProperty;

public interface ICorePropertyDao extends IGenericDao<CoreProperty> {
    CoreProperty retrieveByName(String name);

}
