package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.events.Event;

import java.util.List;

public interface IEventDao extends IGenericDao<Event> {

    List<Event> retrieveLastEvents();

}
