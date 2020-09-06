package app.service.impl;

import app.dao.interfaces.ICalendarStatisticDao;
import app.dao.persistance.IGenericDao;
import app.service.abstraction.AbstractService;
import app.service.interfaces.ICalendarStatistic;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.statisticsmodule.domain.CalendarPerformerEnum;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarStatisticService
        extends AbstractService<AbstractCalendarPerformerStatistic>
        implements ICalendarStatistic {

    private ICalendarStatisticDao dao;

    @Autowired
    public CalendarStatisticService(ICalendarStatisticDao dao) {
        this.dao = dao;
    }

    @Override
    protected IGenericDao<AbstractCalendarPerformerStatistic> getDao() {
        return dao;
    }

    @Override
    public List<AbstractCalendarPerformerStatistic> findByPerformerId(Long id) {
        return dao.findByPerformerId(id);
    }

    @Override
    public List<AbstractCalendarPerformerStatistic> findAllByType(
            CalendarPerformerEnum type) {
        return dao.findAllByType(type);
    }
}
