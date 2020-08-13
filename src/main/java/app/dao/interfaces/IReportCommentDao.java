package app.dao.interfaces;

import app.dao.persistance.IGenericDao;
import app.models.basic.Report;
import app.models.basic.ReportComment;
import java.util.List;

public interface IReportCommentDao extends IGenericDao<ReportComment> {

    List<ReportComment> retrieveByReport(Report report);

    List<ReportComment> retrieveByReportId(Long id);
}
