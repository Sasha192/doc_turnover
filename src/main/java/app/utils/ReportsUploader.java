package app.utils;

import app.customtenant.events.pub.GenericEventPublisher;
import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.ReportComment;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.IBriefDocumentService;
import app.customtenant.service.interfaces.IReportCommentService;
import app.customtenant.service.interfaces.IReportService;
import app.customtenant.service.interfaces.ITaskService;
import app.tenantdefault.models.DocumentEntity;
import dev.morphia.Datastore;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("report_uploader")
public class ReportsUploader {

    private static final Logger EXCEPTION_LOGGER =
            Logger.getLogger("intExceptionLogger");

    private final MaliciousDocumentsScanUtil scan;
    private final Datastore datastore;
    private final IBriefDocumentService documentService;
    private final IReportService reportService;
    private final IReportCommentService commentService;
    private final ITaskService taskService;
    private final GenericEventPublisher<Report> reportPublisher;
    private final GenericEventPublisher<TaskHolderComment> commentPublisher;

    @Autowired
    public ReportsUploader(IBriefDocumentService documentService,
                           MaliciousDocumentsScanUtil scan,
                           Datastore datastore,
                           IReportService reportService,
                           IReportCommentService commentService,
                           ITaskService taskService,
                           GenericEventPublisher<Report> reportPublisher,
                           GenericEventPublisher<TaskHolderComment> commentPublisher) {
        this.documentService = documentService;
        this.scan = scan;
        this.datastore = datastore;
        this.reportService = reportService;
        this.commentService = commentService;
        this.taskService = taskService;
        this.reportPublisher = reportPublisher;
        this.commentPublisher = commentPublisher;
    }

    public boolean upload(Performer performer, Task task,
                          String comment, MultipartFile... mfiles)
            throws IOException {
        if (task == null) {
            return false;
        }
        Report report = getReport(task);
        List<DocumentEntity> documents = scan.checkAndGet(mfiles);
        long performerId = performer.getId();
        for (DocumentEntity entity : documents) {
            datastore.save(entity);
            BriefDocument doc = new BriefDocument(entity, performerId);
            documentService.create(doc);
            report.addDocument(doc);
        }
        reportService.update(report);
        if (comment == null || comment.isEmpty()) {
            comment = performer.getName() + " завантажив " + documents.get(0).getName();
            if (documents.size() > 1) {
                comment = comment + " + " + (documents.size() - 1) + " документів";
            }
        } else {
            comment = StringToUtf8Utils.encodeUtf8(comment);
        }
        publishComment(comment, report, performer);
        reportPublisher.publish(report, performer);
        return true;
    }

    private void publishComment(String comment,
                                Report report,
                                Performer performer) {
        ReportComment reportComment = new ReportComment();
        reportComment.setReport(report);
        reportComment.setAuthorId(performer.getId());
        reportComment.setAuthor(performer);
        reportComment.setComment(comment);
        reportComment.setReport(report);
        commentService.create(reportComment);
        commentPublisher.publish(reportComment, performer);
    }

    private Report getReport(Task task) {
        Report report = null;
        if (task.getReportId() == null) {
            report = new Report();
            reportService.create(report);
            task.setReportId(report.getId());
            taskService.update(task);
        } else {
            report = task.getReport();
        }
        Set<Long> performerIds = task.getPerformerIds();
        performerIds.add(task.getTaskOwnerId());
        report.setPerformerIds(performerIds);
        report.setTaskId(task.getId());
        return report;
    }
}
