package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.events.Event;

import java.util.List;

public interface IEventService extends IOperations<Event> {

    List<Event> retrieveLastEvents();

}
