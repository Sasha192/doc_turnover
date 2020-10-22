package app.customtenant.service.impl;

import app.configuration.spring.constants.Constants;
import app.customtenant.dao.interfaces.IEventDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.events.Event;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IEventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Event> retrieveForPerformer(int page, Long performerId) {
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        return dao.retrieveForPerformer(page, pageSize, performerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countNewEvents(Long performerId) {
        return dao.countNewEvents(performerId);
    }

    @Override
    public void seeAllEvents(Long performerId) {
        dao.seeAllEvents(performerId);
    }
}
