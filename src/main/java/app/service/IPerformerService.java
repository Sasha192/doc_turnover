package app.service;

import app.dao.persistance.IOperations;
import app.models.CoreProperty;
import app.models.Performer;

public interface IPerformerService extends IOperations<CoreProperty> {

    Performer retrieveByName(String name);

}
