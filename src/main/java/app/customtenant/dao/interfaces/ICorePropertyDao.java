package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.CoreProperty;

public interface ICorePropertyDao extends IGenericDao<CoreProperty> {
    CoreProperty retrieveByName(String name);
}
