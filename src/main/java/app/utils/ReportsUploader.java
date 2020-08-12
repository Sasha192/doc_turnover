package app.utils;

import app.models.basic.BriefDocument;
import app.models.basic.Performer;
import app.models.basic.Report;
import app.models.basic.ReportComment;
import app.models.basic.Task;
import app.service.impl.ExecutionService;
import app.service.interfaces.IBriefDocumentService;
import app.service.interfaces.IReportCommentService;
import app.service.interfaces.IReportService;
import app.utils.exceptions.MaliciousFoundException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("report_uploader")
public class ReportsUploader {

    private static final Logger EXCEPTION_LOGGER =
            Logger.getLogger("intExceptionLogger");

    private final IBriefDocumentService documentService;
    private final ExecutionService executionService;
    private final IReportService reportService;
    private final FilesUploader filesUploader;
    private final TodayFolderArchivePathGenerator folderPathGenerator;
    private final IReportCommentService commentService;

    @Autowired
    public ReportsUploader(IBriefDocumentService documentService,
                           ExecutionService executionService,
                           IReportService reportService,
                           @Qualifier("files_uploader") FilesUploader filesUploader,
                           @Qualifier("date_file_path_generator")
                                       TodayFolderArchivePathGenerator folderPathGenerator,
                           IReportCommentService commentService) {
        this.documentService = documentService;
        this.executionService = executionService;
        this.reportService = reportService;
        this.filesUploader = filesUploader;
        this.folderPathGenerator = folderPathGenerator;
        this.commentService = commentService;
    }

    public Report upload(Performer performer, Task task, String comment, MultipartFile... mfiles)
            throws IOException, MaliciousFoundException {
        if (task == null) {
            return null;
        }
        Report report = task.getReport();
        if (report == null) {
            report = new Report();
        }
        report.setTask(task);
        String folderPath = folderPathGenerator.getFolderArchivePath();
        List<File> files = filesUploader.upload(folderPath, mfiles);
        List<BriefDocument> documents =
                FileDocumentsMapperUtil.map(files, folderPath, performer);
        documentService.create(documents);
        report.setDocuments(documents);
        reportService.create(report);
        ReportComment reportComment = new ReportComment();
        reportComment.setReport(report);
        reportComment.setAuthor(performer);
        reportComment.setComment(comment);
        commentService.create(reportComment);
        report.addComment(reportComment);
        return report;
    }
}
