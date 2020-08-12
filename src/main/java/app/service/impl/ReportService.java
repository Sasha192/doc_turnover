package app.service.impl;

import app.dao.interfaces.IReportDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.Report;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IReportService;
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
