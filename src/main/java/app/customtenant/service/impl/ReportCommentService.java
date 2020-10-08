package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IReportCommentDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.ReportComment;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IReportCommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportCommentService
        extends AbstractService<ReportComment>
        implements IReportCommentService {

    private IReportCommentDao dao;

    @Autowired
    public ReportCommentService(IReportCommentDao dao) {
        this.dao = dao;
    }

    @Override
    protected IGenericDao<ReportComment> getDao() {
        return dao;
    }

    public void setDao(final IReportCommentDao dao) {
        this.dao = dao;
    }

    @Override
    public List<ReportComment> retrieveByReport(Report report) {
        return null;
    }

    @Override
    public List<ReportComment> retrieveByReportId(Long id) {
        return null;
    }
}
