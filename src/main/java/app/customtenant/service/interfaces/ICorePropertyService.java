package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.CoreProperty;

public interface ICorePropertyService extends IOperations<CoreProperty> {

    CoreProperty retrieveByName(String name);
}
