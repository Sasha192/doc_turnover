package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.Report;
import app.models.basic.ReportComment;

import java.util.List;

public interface IReportCommentService extends IOperations<ReportComment> {

    List<ReportComment> retrieveByReport(Report report);

    List<ReportComment> retrieveByReportId(Long id);

}
