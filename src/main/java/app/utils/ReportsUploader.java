package app.utils;

import app.customtenant.events.pub.GenericEventPublisher;
import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.IBriefDocumentService;
import app.customtenant.service.interfaces.IReportService;
import app.customtenant.service.interfaces.ITaskService;
import app.tenantdefault.models.DocumentEntity;
import dev.morphia.Datastore;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final ITaskService taskService;
    private final GenericEventPublisher<Report> reportPublisher;
    private final GenericEventPublisher<TaskHolderComment> commentPublisher;

    @Autowired
    public ReportsUploader(IBriefDocumentService documentService,
                           MaliciousDocumentsScanUtil scan,
                           Datastore datastore,
                           IReportService reportService,
                           ITaskService taskService,
                           @Qualifier("report_pub")
                                       GenericEventPublisher<Report> reportPublisher,
                           @Qualifier("comment_pub")
                                       GenericEventPublisher<TaskHolderComment> commentPublisher) {
        this.documentService = documentService;
        this.scan = scan;
        this.datastore = datastore;
        this.reportService = reportService;
        this.taskService = taskService;
        this.reportPublisher = reportPublisher;
        this.commentPublisher = commentPublisher;
    }

    public boolean upload(Performer performer, Task task, MultipartFile... mfiles)
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
        reportPublisher.publish(report, performer);
        return true;
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
