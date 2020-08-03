package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.basic.CoreProperty;

public interface ICorePropertyDao extends IGenericDao<CoreProperty> {
    CoreProperty retrieveByName(String name);

}
