package app.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.dto.CommentDto;
import app.controllers.dto.TaskDto;
import app.controllers.dto.mappers.IEntityDtoMapper;
import app.models.abstr.Comment;
import app.models.basic.Performer;
import app.models.basic.Task;
import app.models.basic.TaskComment;
import app.models.basic.TaskStatus;
import app.models.events.pub.GenericEventPublisher;
import app.models.mysqlviews.BriefTask;
import app.models.serialization.ExcludeStrategies;
import app.security.wrappers.PerformerWrapper;
import app.service.interfaces.IBriefTaskService;
import app.service.interfaces.IStatusService;
import app.service.interfaces.ITaskCommentService;
import app.service.interfaces.ITaskService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TaskNavigationController extends JsonSupportController {

    /*private static final ExclusionStrategy FOR_COMMENT_STRATEGY;*/

    private static final GsonBuilder BUILDER_BRIEF;

    private static final GsonBuilder BUILDER_DETAILS;

    static {
        BUILDER_BRIEF = new GsonBuilder().setPrettyPrinting()
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_COMMENT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_REPORT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_BRIEF_TASK)
                .setDateFormat(Constants.DATE_FORMAT.toPattern());
        BUILDER_DETAILS = new GsonBuilder().setPrettyPrinting()
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_COMMENT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_REPORT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_REPORT)
                .setDateFormat(Constants.DATE_FORMAT.toPattern());
    }

    private IEntityDtoMapper<Task, TaskDto> taskMapper;

    private PerformerWrapper performerWrapper;

    private ITaskService taskService;

    private IStatusService statusService;

    private IBriefTaskService briefTaskService;

    private ITaskCommentService taskCommentService;

    @Autowired
    private GenericEventPublisher<Task> taskPublisher;

    @Autowired
    private GenericEventPublisher<Comment> commentPublisher;

    public TaskNavigationController(PerformerWrapper performerWrapper,
                                    ITaskService taskService,
                                    IStatusService statusService,
                                    IBriefTaskService briefTaskService,
                                    ITaskCommentService taskCommentService,
                                    @Qualifier("task_mapper")
                                            IEntityDtoMapper<Task, TaskDto> taskMapper) {
        this.performerWrapper = performerWrapper;
        this.taskService = taskService;
        this.statusService = statusService;
        this.briefTaskService = briefTaskService;
        this.taskCommentService = taskCommentService;
        this.taskMapper = taskMapper;
    }

    @RequestMapping(value = "/list/{task_status}", method = RequestMethod.GET)
    public void listByDepo(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable(value = "task_status", required = false) String status)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        Long depoId = performer.getDepartmentId();
        List<BriefTask> tasks = null;
        if (status == null) {
            tasks = briefTaskService.findByDepartment(depoId);
        } else {
            tasks = briefTaskService.findByDepartmentAndStatus(depoId, status);
        }
        writeToResponse(response, BUILDER_BRIEF, tasks);
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
        writeToResponse(response, BUILDER_BRIEF, tasks);
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Validated @RequestBody TaskDto dto,
                       HttpServletResponse response,
                       HttpServletRequest request) {
        Task task = taskMapper.getEntity(dto);
        Performer ownerPerformer = performerWrapper.retrievePerformer(request);
        task.setTaskOwner(ownerPerformer);
        task.setTaskOwnerId(ownerPerformer.getId());
        taskService.create(task);
        taskPublisher.publish(task, ownerPerformer);
        sendDefaultJson(response, true, "");
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
        taskComment.setAuthor(performer);
        taskComment.setComment(commentDto.getComment());
        taskComment.setTask(task);
        taskCommentService.create(taskComment);
        commentPublisher.publish(taskComment, performer);
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/comment/list", method = RequestMethod.GET)
    public void showTaskComments(HttpServletResponse response,
                                 @RequestParam("todoId") Long taskId) throws IOException {
        List<? extends Comment> taskComments = taskCommentService.retrieveByTaskId(taskId);
        writeToResponse(response, BUILDER_BRIEF, taskComments);
    }

    @RequestMapping(value = "/details")
    public void getTaskDetails(HttpServletResponse response,
                               @RequestParam("todoId") Long taskId) throws IOException {
        BriefTask task = briefTaskService.findOne(taskId);
        if (task == null) {
            sendDefaultJson(response, false, "");
            return;
        }
        writeToResponse(response, BUILDER_DETAILS, task);
    }
}
