package app.controllers;

import app.configuration.spring.constants.Constants;
import app.models.basic.Performer;
import app.models.basic.Task;
import app.service.interfaces.ITaskService;
import app.utils.ReportsUploader;
import app.utils.exceptions.MaliciousFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    private PerformerWrapper performerWrapper;

    @Autowired
    public ReportController(ReportsUploader uploader,
                            Constants constants,
                            ITaskService taskService,
                            PerformerWrapper performerWrapper) {
        this.uploader = uploader;
        this.constants = constants;
        this.taskService = taskService;
        this.performerWrapper = performerWrapper;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "file", required = false) final MultipartFile[] mfiles,
                       @RequestParam("comment") final String comment,
                       @RequestParam("todoId") final Integer taskId,
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
            uploader.upload(performer, task, comment, mfiles);
        } catch (MaliciousFoundException ioex) {
            sendDefaultJson(response, false, ioex.getMessage());
        } catch (IOException e) {
            sendDefaultJson(response, false, "Internal Server Error. Please try later.");
        }
        sendDefaultJson(response, true, "");
    }
}
