package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IEventDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.events.Event;
import app.customtenant.events.PerformerEventAgent;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IEventService;
import java.util.Date;
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
    public void seeEvent(Long eventId, Long performerId) {
        dao.seeEvent(eventId, performerId);
    }

    @Override
    public List<PerformerEventAgent> retrieveForPerformer(Long performerId) {
        return dao.retrieveForPerformer(performerId);
    }

    @Override
    public Long countNewEvents(Long performerId) {
        return dao.countNewEvents(performerId);
    }

    @Override
    public void seeAllEvents(Long performerId) {
        dao.seeAllEvents(performerId);
    }

    @Override
    public List<PerformerEventAgent> retrieveAfterLastReceivedForPerformerId(
            Long id, Long lastReceivedEventId
    ) {
        return dao.retrieveAfterLastReceivedForPerformerId(id, lastReceivedEventId);
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
