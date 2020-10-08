package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.ICalendarStatisticDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.domain.CalendarPerformerEnum;
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
