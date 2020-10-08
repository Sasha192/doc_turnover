package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.ReportComment;
import java.util.List;

public interface IReportCommentService extends IOperations<ReportComment> {

    List<ReportComment> retrieveByReport(Report report);

    List<ReportComment> retrieveByReportId(Long id);

}
