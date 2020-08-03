package app.service.impl;

import app.dao.interfaces.IEventDao;
import app.dao.interfaces.IPerformerDao;
import app.dao.persistance.IGenericDao;
import app.models.events.Event;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
}
