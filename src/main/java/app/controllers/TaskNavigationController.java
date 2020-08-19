package app.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.dto.CommentDto;
import app.controllers.dto.TaskDto;
import app.controllers.dto.mappers.IEntityDtoMapper;
import app.models.abstr.TaskHolderComment;
import app.models.basic.Performer;
import app.models.basic.Task;
import app.models.basic.TaskComment;
import app.models.basic.TaskStatus;
import app.models.events.pub.GenericEventPublisher;
import app.models.mysqlviews.BriefTask;
import app.security.models.SimpleRole;
import app.service.interfaces.IBriefTaskService;
import app.service.interfaces.IStatusService;
import app.service.interfaces.ITaskCommentService;
import app.service.interfaces.ITaskService;
import java.io.IOException;
import java.util.List;
import java.util.Set;
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

    private IEntityDtoMapper<Task, TaskDto> taskMapper;

    private PerformerWrapper performerWrapper;

    private ITaskService taskService;

    private IStatusService statusService;

    private IBriefTaskService briefTaskService;

    private ITaskCommentService taskCommentService;

    private final GenericEventPublisher<Task> taskPublisher;

    private final GenericEventPublisher<TaskHolderComment> commentPublisher;

    public TaskNavigationController(PerformerWrapper performerWrapper,
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
        this.statusService = statusService;
        this.briefTaskService = briefTaskService;
        this.taskCommentService = taskCommentService;
        this.taskMapper = taskMapper;
        this.taskPublisher = taskPublisher;
        this.commentPublisher = commentPublisher;
    }

    @RequestMapping(value = "/list/{task_status}", method = RequestMethod.GET)
    public void listByDepo(HttpServletResponse response, HttpServletRequest request,
                           @PathVariable(value = "task_status", required = false) String status,
                           @PathVariable(value = "pageId", required = false) Integer pageId)
            throws IOException {
        // @TODO : PageId!!!
        Performer performer = performerWrapper.retrievePerformer(request);
        Long depoId = performer.getDepartmentId();
        List<BriefTask> tasks = null;
        Set<SimpleRole> roles = performer.getRoles();
        if (status == null) {
            tasks = briefTaskService.findByDepartment(depoId);
        } else {
            if (allowAllOp(roles)) {
                tasks = briefTaskService.findByStatus(status);
            } else {
                tasks = briefTaskService.findByDepartmentAndStatus(depoId, status);
            }
        }
        writeToResponse(response, Constants.BUILDER_BRIEF, tasks);
    }

    private boolean allowAllOp(Set<SimpleRole> roles) {
        return roles.contains(SimpleRole.ADMIN)
                || roles.contains(SimpleRole.G_MANAGER)
                || roles.contains(SimpleRole.SECRETARY);
    }

    @RequestMapping(value = "/my/list/{task_status}", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable(value = "task_status", required = false) String status)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<BriefTask> tasks = null;
        if (status == null) {
            tasks = briefTaskService.findByPerformer(performer.getId());
        } else {
            tasks = briefTaskService.findByPerformerAndStatus(performer.getId(), status);
        }
        writeToResponse(response, Constants.BUILDER_BRIEF, tasks);
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Validated @RequestBody TaskDto dto,
                       HttpServletResponse response,
                       HttpServletRequest request) {
        Performer ownerPerformer = performerWrapper.retrievePerformer(request);
        if (allowOp(ownerPerformer.getRoles())) {
            Task task = taskMapper.getEntity(dto);
            task.setTaskOwner(ownerPerformer);
            task.setTaskOwnerId(ownerPerformer.getId());
            taskService.create(task);
            taskPublisher.publish(task, ownerPerformer);
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Access Denied");
        }
    }

    @RequestMapping(value = "/modify/priority")
    public void changePriority(@RequestParam("priority") String newPriority,
                               @RequestParam("task_id") Integer taskId) {
        Task task = taskService.findOne(taskId);
        task.setPriority(newPriority);
        taskService.update(task);
    }

    @RequestMapping(value = "/modify/status", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeStatus(@RequestParam("status") String newStatus,
                             @RequestParam("task_id") Integer taskId,
                             HttpServletResponse response) {
        Task task = taskService.findOne(taskId);
        TaskStatus status = statusService.findByTitle(newStatus);
        task.setStatus(status);
        taskService.update(task);
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/comment/add", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTaskComment(@Validated @RequestBody CommentDto commentDto,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        TaskComment taskComment = new TaskComment();
        Task task = taskService.findOne(commentDto.getTodoId());
        if (task == null) {
            return;
        }
        Performer performer = performerWrapper.retrievePerformer(request);
        taskComment.setAuthorId(performer.getId());
        taskComment.setComment(commentDto.getComment());
        taskComment.setTaskId(task.getId());
        taskCommentService.create(taskComment);
        commentPublisher.publish(taskComment, performer);
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
        BriefTask task = briefTaskService.findOne(taskId);
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
        Set<SimpleRole> roles = performer.getRoles();
        if (allowOp(roles)) {
            Task task = taskService.findOne(taskId);
            try {
                for (String docId : docIds) {
                    final Long id = Long.valueOf(docId);
                    task.addDocumentId(id);
                }
                taskService.update(task);
                if (comment != null) {
                    if (comment.isEmpty()) {
                        comment = performer.getName() + " вніс(внесла) зміни до завдання.";
                    }
                    TaskComment taskComment = new TaskComment();
                    taskComment.setTaskId(taskId);
                    taskComment.setComment(comment);
                    taskComment.setAuthorId(performer.getId());
                    taskCommentService.create(taskComment);
                    commentPublisher.publish(taskComment, performer);
                }
                sendDefaultJson(response, true, "");
            } catch (NumberFormatException ex) {
                LOGGER.error("NUMBER FORMAT EXCEPTION " + ex.getMessage());
                LOGGER.error(ex.getStackTrace());
                sendDefaultJson(response, false, "Wrong Format! Who r u ?");
            }
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
        Set<SimpleRole> roles = performer.getRoles();
        if (allowOp(roles)) {
            taskService.updateNameDescription(newName, description, taskId);
        } else {
            sendDefaultJson(response, false, "Access Denied");
            return;
        }
    }

    private boolean allowOp(Set<SimpleRole> roles) {
        return roles.contains(SimpleRole.G_MANAGER)
                || roles.contains(SimpleRole.ADMIN)
                || roles.contains(SimpleRole.MANAGER);
    }
}
