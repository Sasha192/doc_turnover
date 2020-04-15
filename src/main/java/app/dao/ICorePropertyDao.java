package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.CoreProperty;

import java.rmi.NoSuchObjectException;

public interface ICorePropertyDao extends IGenericDao<CoreProperty> {

    CoreProperty retrieveByName(String name) throws NoSuchObjectException;

}
