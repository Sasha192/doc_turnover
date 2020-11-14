package app.controllers.customtenant;

import app.configuration.spring.constants.Constants;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.IReportService;
import app.customtenant.service.interfaces.ITaskService;
import app.security.models.SimpleRole;
import app.security.wrappers.IPerformerWrapper;
import app.utils.ReportsUploader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/report")
public class ReportController extends JsonSupportController {

    private final ReportsUploader uploader;

    private final Constants constants;

    private final ITaskService taskService;

    private IPerformerWrapper performerWrapper;

    private final IReportService reportService;

    @Autowired
    public ReportController(ReportsUploader uploader,
                            Constants constants,
                            ITaskService taskService,
                            IPerformerWrapper performerWrapper,
                            IReportService reportService) {
        this.uploader = uploader;
        this.constants = constants;
        this.taskService = taskService;
        this.performerWrapper = performerWrapper;
        this.reportService = reportService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "files", required = false) final MultipartFile[] mfiles,
                       @RequestParam("taskId") final Long taskId,
                       HttpServletResponse response,
                       HttpServletRequest request) {
        int max = constants.get(Constants.MAX_FILES_UPLOAD).getIntValue();
        if (mfiles.length > max) {
            sendDefaultJson(response, false,
                    "Too many files. You can upload no more " + max
            );
            return;
        }
        try {
            Performer performer = performerWrapper
                    .retrievePerformer(request);
            Task task = taskService.findOne(taskId);
            long perfId = performer.getId();
            if (task.getTaskOwnerId().equals(perfId)
                    || task.getPerformerIds().contains(perfId)) {
                uploader.upload(performer, task, mfiles);
            }
        } catch (IOException e) {
            sendDefaultJson(response, false, "Internal Server Error. Please try later.");
        }
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/doc/remove", method = RequestMethod.POST)
    public void docRemove(@RequestBody String body,
                       HttpServletResponse response,
                       HttpServletRequest request) {
        JsonElement element = JsonParser.parseString(body);
        Performer performer = performerWrapper.retrievePerformer(request);
        if (performer != null && element.isJsonObject()) {
            try {
                if (allowDocRemove(performer.getRoles())) {
                    JsonObject o = element.getAsJsonObject();
                    Long reportId = o.get("reportId").getAsLong();
                    Long docId = o.get("docId").getAsLong();
                    Report report = reportService.findOne(reportId);
                    if (report != null) {
                        report.getDocIds().remove(docId);
                        reportService.update(report);
                    }
                }
            } catch (Exception e) {
                sendDefaultJson(response, false, "");
            }
        }
        sendDefaultJson(response, true, "");
    }

    private boolean allowDocRemove(SimpleRole roles) {
        return SimpleRole.G_MANAGER.equals(roles)
                || SimpleRole.MANAGER.equals(roles)
                || SimpleRole.ADMIN.equals(roles);
    }
}
