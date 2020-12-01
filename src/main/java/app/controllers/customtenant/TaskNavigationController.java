package app.controllers.customtenant;

import app.configuration.spring.constants.Constants;
import app.controllers.dto.CommentDto;
import app.controllers.dto.TaskDto;
import app.customtenant.events.pub.GenericEventPublisher;
import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.TaskComment;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.IPerformerService;
import app.customtenant.service.interfaces.ITaskCommentService;
import app.customtenant.service.interfaces.ITaskService;
import app.security.models.SimpleRole;
import app.security.wrappers.IPerformerWrapper;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/com/task")
/**
 * This request mappings could get access only
 * @see TaskNavigationController#allowOp(Set)
 */
public class TaskNavigationController extends JsonSupportController {

    private static final GsonBuilder DETAILS_BUILDER = new GsonBuilder()
            .setPrettyPrinting()
            .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
            .setDateFormat(Constants.DATE_FORMAT.toPattern());

    private static final Logger LOGGER = Logger.getLogger("errorLogger");

    private IPerformerWrapper performerWrapper;

    private ITaskService taskService;

    private ITaskCommentService taskCommentService;

    private final GenericEventPublisher<Task> taskPublisher;

    private final GenericEventPublisher<TaskHolderComment> commentPublisher;

    private final IPerformerService performerService;

    public TaskNavigationController(IPerformerWrapper performerWrapper,
                                    ITaskService taskService,
                                    ITaskCommentService taskCommentService,
                                    @Qualifier("task_pub")
                                            GenericEventPublisher<Task> taskPublisher,
                                    @Qualifier("comment_pub")
                                            GenericEventPublisher<TaskHolderComment>
                                            commentPublisher,
                                    IPerformerService performerService) {
        this.performerWrapper = performerWrapper;
        this.taskService = taskService;
        this.taskCommentService = taskCommentService;
        this.taskPublisher = taskPublisher;
        this.commentPublisher = commentPublisher;
        this.performerService = performerService;
    }

    @RequestMapping(value = "/list/{page_id}", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable(value = "page_id", required = false) Integer pageId,
                     @RequestParam(value = "depId", required = false) Long requestedDepId)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<Task> tasks = null;
        SimpleRole roles = performer.getRoles();
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        if (allowAllOp(roles)) {
            if (requestedDepId != null) {
                tasks = taskService.findByDepartment(pageId, pageSize, requestedDepId);
            } else {
                tasks = taskService.findAll(pageId, pageSize);
            }
        } else if (roles.equals(SimpleRole.MANAGER)) {
            tasks = taskService.findByDepartment(pageId, pageSize,
                    performer.getDepartmentId());
        } else {
            tasks = taskService.findByPerformer(pageId, pageSize,
                    performer.getId());
        }
        writeToResponse(response, Constants.BUILDER_BRIEF, tasks);
    }

    @RequestMapping(value = "/list/maxpages", method = RequestMethod.GET)
    public void maxPages(HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        writeToResponse(response, Constants.BUILDER_BRIEF, "10");
    }

    @RequestMapping(value = "/list/{task_status}/{page_id}", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable(value = "task_status") String status,
                     @PathVariable(value = "page_id") Integer page,
                     @RequestParam(value = "perf_id", required = false) Long performerId)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<Task> tasks = null;
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        if (allowOp(performer.getRoles())) {
            if (performerId == null) {
                tasks = taskService.findByStatus(
                        page, pageSize, TaskStatus.getByName(status)
                );
            } else {
                tasks = taskService.findByPerformerAndStatus(
                        page, pageSize, performerId, TaskStatus.getByName(status)
                );
            }
        } else {
            tasks = taskService.findByPerformerAndStatus(
                    page, pageSize, performer.getId(), TaskStatus.getByName(status)
            );
        }
        writeToResponse(response, Constants.BUILDER_BRIEF, tasks);
    }

    private boolean allowAllOp(SimpleRole roles) {
        return roles.equals(SimpleRole.ADMIN)
                || roles.equals(SimpleRole.G_MANAGER);
    }

    @RequestMapping(value = "/doc/remove",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void remove(@RequestBody String body,
                       HttpServletResponse response,
                       HttpServletRequest request) {
        JsonObject object = JsonParser.parseString(body).getAsJsonObject();
        try {
        Long taskId = object.get("taskId").getAsLong();
        Long docId = object.get("docId").getAsLong();
        Task task = taskService.findOne(taskId);
        if (task != null) {
                Performer ownerPerformer = task.getTaskOwner();
                if (allowOp(ownerPerformer.getRoles())) {
                    task.getDocumentsIds().remove(docId);
                    taskService.update(task);
                } else {
                    sendDefaultJson(response, false, "Access Denied");
                }
            }
        } catch (Exception ex) {
            sendDefaultJson(response, false, "Wrong data passed");
        }
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Validated(TaskDto.NewTask.class) @RequestBody TaskDto dto,
                       HttpServletResponse response,
                       HttpServletRequest request) {
        Performer ownerPerformer = performerWrapper.retrievePerformer(request);
        try {
            if (allowOp(ownerPerformer.getRoles())) {
                Task task = new Task(dto, ownerPerformer.getId());
                task.setTaskOwner(ownerPerformer);
                taskService.create(task);
                taskPublisher.publish(task, ownerPerformer);
                sendDefaultJson(response, true, "");
            } else {
                sendDefaultJson(response, false, "Access Denied");
            }
        } catch (ParseException ex) {
            sendDefaultJson(response, false, "Wrong data passed");
        }
    }

    @RequestMapping(value = "/modify/status", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeStatus(@RequestBody String body,
                             HttpServletResponse response, HttpServletRequest request) {
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendDefaultJson(response, false, "Помилка. Передано некоректні дані");
            return;
        }
        JsonObject o = element.getAsJsonObject();
        String newStatus = o.get("status").getAsString();
        long taskId = o.get("taskId").getAsLong();
        Task task = taskService.findOne(taskId);
        TaskStatus status = TaskStatus.getByName(newStatus);
        Performer performer = performerWrapper.retrievePerformer(request);
        if (task.getPerformerIds().contains(performer.getId())
                || performer.getId().equals(task.getTaskOwnerId())) {
            task.setStatus(status);
            taskService.update(task);
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Помилка. Немає прав");
        }
    }

    @RequestMapping(value = "/modify/deadline", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeDeadline(@RequestBody String body,
                             HttpServletResponse response, HttpServletRequest request) {
        JsonElement element = JsonParser.parseString(body);
        if (element.isJsonObject()) {
            try {
                JsonObject o = element.getAsJsonObject();
                Long taskId = o.get("taskId").getAsLong();
                String newDeadlineStr = o.get("date").getAsString();
                Task task = taskService.findOne(taskId);
                Date deadline = Constants.DATE_FORMAT.parse(newDeadlineStr);
                Performer performer = performerWrapper.retrievePerformer(request);
                if (task.getTaskOwnerId().equals(performer.getId())) {
                    task.setDeadlineDate(deadline);
                    taskService.update(task);
                    sendDefaultJson(response, true, "");
                }
            } catch (ParseException e) {
                ;
            }
        }
        sendDefaultJson(response, false, "Access Denied");
    }

    @RequestMapping(value = "/modify/control", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeControlDate(@RequestBody String body,
                               HttpServletResponse response, HttpServletRequest request) {
        JsonElement element = JsonParser.parseString(body);
        if (element.isJsonObject()) {
            try {
                JsonObject o = element.getAsJsonObject();
                Long taskId = o.get("taskId").getAsLong();
                String newDeadlineStr = o.get("date").getAsString();
                Task task = taskService.findOne(taskId);
                Date deadline = Constants.DATE_FORMAT.parse(newDeadlineStr);
                Performer performer = performerWrapper.retrievePerformer(request);
                if (task.getTaskOwnerId().equals(performer.getId())) {
                    task.setControlDate(deadline);
                    taskService.update(task);
                    sendDefaultJson(response, true, "");
                }
            } catch (ParseException e) {
                ;
            }
        }
        sendDefaultJson(response, false, "Access Denied");
    }

    @RequestMapping(value = "/performer/add", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addPerformer(@RequestBody String body,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        JsonElement elem = JsonParser.parseString(body);
        if (elem.isJsonObject()) {
            JsonObject o = elem.getAsJsonObject();
            try {
                Long taskId = o.get("taskId").getAsLong();
                Long performerId = o.get("performerId").getAsLong();
                Task task = taskService.findOne(taskId);
                if (task != null) {
                    if (performerService.findOne(performerId) != null) {
                        task.getPerformerIds().add(performerId);
                        taskService.update(task);
                    }
                }
                sendDefaultJson(response, true, "");
                return;
            } catch (Exception e) {
                ;
            }
        }
        sendDefaultJson(response, false, "Wrong request");
    }

    @RequestMapping(value = "/performer/remove", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removePerformer(@RequestBody String body,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        JsonElement elem = JsonParser.parseString(body);
        if (elem.isJsonObject()) {
            JsonObject o = elem.getAsJsonObject();
            try {
                Long taskId = o.get("taskId").getAsLong();
                Long performerId = o.get("performerId").getAsLong();
                Task task = taskService.findOne(taskId);
                if (task != null) {
                    if (performerService.findOne(performerId) != null) {
                        task.getPerformerIds().remove(performerId);
                        taskService.update(task);
                    }
                }
                sendDefaultJson(response, true, "");
                return;
            } catch (Exception e) {
                ;
            }
        }
        sendDefaultJson(response, false, "Wrong request");
    }

    @RequestMapping(value = "/comment/add", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTaskComment(@Validated @RequestBody CommentDto commentDto,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        TaskComment taskComment = new TaskComment();
        Task task = taskService.findOne(commentDto.getTaskId());
        if (task == null) {
            sendDefaultJson(response, false, "");
        }
        Performer performer = performerWrapper.retrievePerformer(request);
        long perfId = performer.getId();
        if (task.getPerformerIds().contains(perfId)
                || task.getTaskOwnerId().equals(perfId)) {
            taskComment.setAuthorId(performer.getId());
            taskComment.setComment(commentDto.getComment());
            taskComment.setTaskId(task.getId());
            taskComment.setTask(task);
            taskCommentService.create(taskComment);
            commentPublisher.publish(taskComment, performer);
        }
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/comment/remove", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeTaskComment(@RequestBody String body,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        JsonObject o = JsonParser.parseString(body).getAsJsonObject();
        Long taskId = o.get("taskId").getAsLong();
        Long commentId = o.get("commentId").getAsLong();
        Performer performer = performerWrapper.retrievePerformer(request);
        TaskComment comment = taskCommentService.findOne(commentId);
        if (performer != null
                && comment.getAuthorId().equals(performer.getId())) {
            Task task = taskService.findOne(taskId);
            if (task != null) {
                task.getCommentIds().remove(commentId);
            }
        }
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/comment/list", method = RequestMethod.GET)
    public void showTaskComments(HttpServletResponse response,
                                 @RequestParam("todoId") Long taskId) throws IOException {
        List<? extends TaskComment> taskComments = taskCommentService.retrieveByTaskId(taskId);
        writeToResponse(response, Constants.BUILDER_BRIEF, taskComments);
    }

    @RequestMapping(value = "/details")
    public void getTaskDetails(HttpServletResponse response,
                               @RequestParam("todoId") Long taskId) throws IOException {
        Task task = taskService.findOne(taskId);
        if (task == null) {
            sendDefaultJson(response, false, "");
            return;
        }
        writeToResponse(response, DETAILS_BUILDER, task);
    }

    @RequestMapping(value = "/modify/docs",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    // @TODO : I CHANGED taskIds ->
    public void modifyTask(HttpServletResponse response,
                           HttpServletRequest request,
                           @RequestParam("todoIds") Long[] taskIds,
                           @RequestParam("docIds") String[] docIds,
                           @RequestParam(value = "comment", required = false)
                                   String comment)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        SimpleRole roles = performer.getRoles();
        for (Long taskId : taskIds) {
            if (allowOp(roles)) {
                Task task = taskService.findOne(taskId);
                try {
                    for (String docId : docIds) {
                        final Long id = Long.valueOf(docId);
                        task.addDocumentId(id);
                    }
                } catch (NumberFormatException ex) {
                    LOGGER.error("NUMBER FORMAT EXCEPTION " + ex.getMessage());
                    LOGGER.error(ex.getStackTrace());
                    sendDefaultJson(response, false, "Access Denied");
                }
                taskService.update(task);
                if (comment == null || comment.isEmpty()) {
                    comment = performer.getName() + " вніс(внесла) зміни до завдання.";
                }
                TaskComment taskComment = new TaskComment();
                taskComment.setTaskId(taskId);
                taskComment.setComment(comment);
                taskComment.setAuthorId(performer.getId());
                taskComment.setTask(task);
                taskCommentService.create(taskComment);
                commentPublisher.publish(taskComment, performer);
            } else {
                sendDefaultJson(response, false, "Access Denied");
                return;
            }
        }
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/modify/name")
    public void modifyTaskName(@RequestParam("todoId") Long taskId,
                               @RequestParam(value = "name", required = false)
                                       String newName,
                               @RequestParam(value = "description", required = false)
                                       String description,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        Performer performer = performerWrapper
                .retrievePerformer(request);
        SimpleRole roles = performer.getRoles();
        if (allowOp(roles)) {
            Task task = taskService.findOne(taskId);
            if (newName != null) {
                task.setToDo(newName);
            }
            if (description != null) {
                task.setDescription(description);
            }
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Access Denied");
        }
    }

    private boolean allowOp(SimpleRole roles) {
        return roles.equals(SimpleRole.G_MANAGER)
                || roles.equals(SimpleRole.ADMIN)
                || roles.equals(SimpleRole.MANAGER);
    }
}
