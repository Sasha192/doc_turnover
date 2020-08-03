package app.service.impl;

import app.dao.interfaces.IReportCommentDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.Report;
import app.models.basic.ReportComment;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IReportCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
