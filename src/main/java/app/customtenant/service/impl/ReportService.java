package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IReportDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Report;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportService
        extends AbstractService<Report>
        implements IReportService {

    private IReportDao dao;

    @Autowired
    public ReportService(IReportDao dao) {
        this.dao = dao;
    }

    @Override
    protected IGenericDao<Report> getDao() {
        return dao;
    }
}
