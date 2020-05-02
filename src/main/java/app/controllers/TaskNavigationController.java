package app.controllers;

import app.controllers.responses.ResponseJsonText;
import app.models.BriefTask;
import app.models.Performer;
import app.models.Task;
import app.models.TaskStatus;
import app.models.serialization.ExcludeStrategies;
import app.service.ICorePropertyService;
import app.service.IStatusService;
import app.service.ITaskService;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/task")
public class TaskNavigationController extends JsonSupportController {
    /*


        ADD FILTER AUTHENTICATION + AUTHERIZATION


    */

    private static final String PERF_ATTR = "performer_object";

    private static final String ARCHIVE_PATH_NAME = "ARCHIVE_PATH";

    private static final String EXTENSION_FILE = ".docx";

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd_MM_yyyy");

    private static final String SEPARATOR_FILE_SYSTEM = null;

    private static final String FILE_PATH_SEPARATOR = File.separator;

    private static final String UNDERSCORE = "_";

    private static final Logger LOGGER = Logger.getLogger(TaskNavigationController.class);

    private static final String FILE_TRANSFER_ERROR =
            "==================FILE TRANSFER ERROR==================";

    private static String ARCHIVE_PATH;

    @Autowired
    private ICorePropertyService corePropertyService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IStatusService statusService;

    @RequestMapping(value = "/acrhive/render")
    public String render(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        Performer performer = (Performer) request.getSession().getAttribute(PERF_ATTR);
        int performerId = performer.getId();
        List<TaskStatus> perfStatus = performer.getStatus();
        map.put("boards", perfStatus);
        map.put("performer", performer);
        List<Task> staticTasks = taskService.findByPerformerIdStaticStatus(performerId);
        staticTasksToMap(staticTasks, map);
        return "view";
    }

    private void staticTasksToMap(List<Task> staticTasks, ModelMap map) {
        List<Task> active = new LinkedList<>();
        List<Task> noActive = new LinkedList<>();
        List<Task> done = new LinkedList<>();

        staticTasks.parallelStream().forEach(
                task -> {
                    switch (task.getStatusString()) {
                        case "active":
                            active.add(task);
                            break;
                        case "no_active":
                            noActive.add(task);
                            break;
                        case "done":
                            done.add(task);
                            break;
                        default:
                            throw new IllegalArgumentException("Wrong status");
                    }
                }
        );
        map.put("active", active);
        map.put("done", done);
        map.put("no_active", noActive);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response)
            throws IOException {
        List<Task> tasks = taskService.findAll();
        GsonBuilder builder = new GsonBuilder()
                .setExclusionStrategies(ExcludeStrategies.ONE_TO_MANY)
                .setPrettyPrinting();
        writeToResponse(response, builder, BriefTask.toBrief(tasks));
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public void listPage(HttpServletResponse response,
                         @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
                         @RequestParam(name = "page_size", defaultValue = "30") Integer pageSize)
            throws IOException {
        List<Task> tasks = taskService.findAll(pageNumber, pageSize);
        GsonBuilder builder = new GsonBuilder()
                .setExclusionStrategies(ExcludeStrategies.ONE_TO_MANY)
                .setPrettyPrinting();
        writeToResponse(response, builder, tasks);
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public void listFilters(@RequestBody Map<String, String> filters, HttpServletResponse res)
            throws IOException {
        List<Task> tasks = taskService.findBy(filters);
        GsonBuilder builder = new GsonBuilder()
                .setExclusionStrategies(ExcludeStrategies.ONE_TO_MANY)
                .setPrettyPrinting();
        writeToResponse(res, builder, tasks);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonText upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("performer") String performerStr,
                                   @RequestParam("department") String departmentStr,
                                   ModelMap modelMap) {
        String date = LocalDate.now().format(DATE_TIME_FORMATTER);
        String filePath = ARCHIVE_PATH
                .concat(FILE_PATH_SEPARATOR)
                .concat(departmentStr)
                .concat(UNDERSCORE)
                .concat(performerStr)
                .concat(UNDERSCORE)
                .concat(date)
                .concat(EXTENSION_FILE);
        File fileToSave = new File(filePath);
        return new ResponseJsonText(Boolean.TRUE, "Everything is Alright");
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public void download(@RequestParam("doc_id") String docId,
                         HttpServletResponse response) throws Exception {
        /*Long id = Long.valueOf(docId);
        Task task = taskService.findOne(id);
        File doc = new File(task.getPath());
        InputStream in = new FileInputStream(doc);
        toResponseDocxData(response, in.readAllBytes(), doc.getName());*/
    }

    private void toResponseDocxData(HttpServletResponse response,
                                    byte[] data, String name)
            throws IOException {
        /*response.setContentType("application/msword");
        response.setHeader("Content-disposition", "inline; filename=" + name);
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.getOutputStream().flush();*/
    }
}
