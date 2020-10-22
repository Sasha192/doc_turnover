package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.events.Event;
import java.util.List;

public interface IEventDao extends IGenericDao<Event> {

    Long countNewEvents(Long performerId);

    List<Event> retrieveForPerformer(int page, int pageSize, Long performerId);

    void seeAllEvents(Long performerId);
}
