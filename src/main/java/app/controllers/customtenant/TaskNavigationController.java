package app.controllers.customtenant;

import app.configuration.spring.constants.Constants;
import app.controllers.dto.CommentDto;
import app.controllers.dto.TaskDto;
import app.controllers.dto.mappers.IEntityDtoMapper;
import app.customtenant.events.pub.GenericEventPublisher;
import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.TaskComment;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.IBriefTaskService;
import app.customtenant.service.interfaces.IStatusService;
import app.customtenant.service.interfaces.ITaskCommentService;
import app.customtenant.service.interfaces.ITaskService;
import app.security.models.SimpleRole;
import app.security.wrappers.IPerformerWrapper;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping(value = "/task")
/**
 * This request mappings could get access only
 * @see TaskNavigationController#allowOp(Set)
 */
public class TaskNavigationController extends JsonSupportController {

    private static final Logger LOGGER = Logger.getLogger("errorLogger");

    private IPerformerWrapper performerWrapper;

    private ITaskService taskService;

    private ITaskCommentService taskCommentService;

    private final GenericEventPublisher<Task> taskPublisher;

    private final GenericEventPublisher<TaskHolderComment> commentPublisher;

    public TaskNavigationController(IPerformerWrapper performerWrapper,
                                    ITaskService taskService,
                                    IStatusService statusService,
                                    IBriefTaskService briefTaskService,
                                    ITaskCommentService taskCommentService,
                                    @Qualifier("task_mapper")
                                            IEntityDtoMapper<Task, TaskDto> taskMapper,
                                    GenericEventPublisher<Task> taskPublisher,
                                    GenericEventPublisher<TaskHolderComment> commentPublisher) {
        this.performerWrapper = performerWrapper;
        this.taskService = taskService;
        this.taskCommentService = taskCommentService;
        this.taskPublisher = taskPublisher;
        this.commentPublisher = commentPublisher;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable(value = "pageId", required = false) Integer pageId)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        Long depoId = performer.getDepartmentId();
        List<Task> tasks = null;
        SimpleRole roles = performer.getRoles();
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        if (allowAllOp(roles)) {
            tasks = taskService.findAll(pageId, pageSize);
        } else if (roles.equals(SimpleRole.MANAGER)) {
            tasks = taskService.findByDepartment(pageId, pageSize,
                    performer.getDepartmentId());
        } else {
            tasks = taskService.findByPerformer(pageId, pageSize,
                    performer.getId());
        }
        writeToResponse(response, Constants.BUILDER_BRIEF, tasks);
    }

    @RequestMapping(value = "/my/list/{task_status}", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable(value = "task_status") String status,
                     @RequestParam(value = "page_id") Integer page,
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

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Validated @RequestBody TaskDto dto,
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

    @RequestMapping(value = "/modify/status", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeStatus(@RequestParam("status") String newStatus,
                             @RequestParam("task_id") Long taskId,
                             HttpServletResponse response, HttpServletRequest request) {
        Task task = taskService.findOne(taskId);
        TaskStatus status = TaskStatus.getByName(newStatus);
        Performer performer = performerWrapper.retrievePerformer(request);
        if (task.getPerformerIds().contains(performer.getId())) {
            taskService.update(task);
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Access Denied");
        }
    }

    @RequestMapping(value = "/comment/add", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTaskComment(@Validated @RequestBody CommentDto commentDto,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        TaskComment taskComment = new TaskComment();
        Task task = taskService.findOne(commentDto.getTodoId());
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
        writeToResponse(response, Constants.BUILDER_DETAILS, task);
    }

    @RequestMapping(value = "/modify/docs",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void modifyTask(HttpServletResponse response,
                           HttpServletRequest request,
                           @RequestParam("todoId") Long taskId,
                           @RequestParam("docIds") String[] docIds,
                           @RequestParam(value = "comment", required = false)
                                   String comment)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        SimpleRole roles = performer.getRoles();
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
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Access Denied");
        }
    }

    @RequestMapping(value = "/modify/name")
    public void modifyTaskName(@RequestParam("todoId") Long taskId,
                               @RequestParam(value = "name", required = false) String newName,
                               @RequestParam(value = "description", required = false)
                                       String description,
                               HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {
        Performer performer = performerWrapper
                .retrievePerformer(request);
        SimpleRole roles = performer.getRoles();
        if (allowOp(roles)) {
            taskService.updateNameDescription(newName, description, taskId);
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Access Denied");
            return;
        }
    }

    private boolean allowOp(SimpleRole roles) {
        return roles.equals(SimpleRole.G_MANAGER)
                || roles.equals(SimpleRole.ADMIN)
                || roles.equals(SimpleRole.MANAGER);
    }
}
