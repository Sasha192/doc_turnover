package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.ReportComment;
import java.util.List;

public interface IReportCommentDao extends IGenericDao<ReportComment> {

    List<ReportComment> retrieveByReport(Report report);

    List<ReportComment> retrieveByReportId(Long id);
}
