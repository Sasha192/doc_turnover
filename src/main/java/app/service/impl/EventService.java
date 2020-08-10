package app.service.impl;

import app.dao.interfaces.IEventDao;
import app.dao.persistance.IGenericDao;
import app.models.events.Event;
import app.models.events.PerformerEventAgent;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IEventService;
import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService extends AbstractService<Event>
        implements IEventService {

    private IEventDao dao;

    @Autowired
    public EventService(IEventDao dao) {
        this.dao = dao;
    }

    public void setDao(final IEventDao dao) {
        this.dao = dao;
    }

    @Override
    protected IGenericDao<Event> getDao() {
        return dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> retrieveLastEvents() {
        return dao.retrieveLastEvents();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformerEventAgent> retrieveLastEventsForPerformerId(Long performerId) {
        return dao.retrieveLastEventsForPerformerId(performerId);
    }

    @Override
    @Deprecated
    /**
     * Not implemented yet
     */
    public List<PerformerEventAgent> retrieveEventsForPerformerIdAfterDate(Long performerId,
                                                                           Date afterDate) {
        return null;
    }

    @Override
    @Deprecated
    /**
     * Not implemented yet
     */
    public List<PerformerEventAgent> retrieveEventsForPerformerIdBeforeDate(Long performerId,
                                                                            Date beforeDate) {
        return null;
    }
}
