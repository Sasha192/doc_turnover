package app.controllers;

import app.controllers.dto.IEntityDtoMapper;
import app.controllers.dto.TaskDto;
import app.models.BriefTask;
import app.models.Performer;
import app.models.Task;
import app.security.utils.PerformerWrapper;
import app.service.IBriefTaskService;
import app.service.ITaskService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("task_mapper")
    private IEntityDtoMapper<Task, TaskDto> taskMapper;

    @Autowired
    private PerformerWrapper performerWrapper;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IBriefTaskService briefTaskService;

    @RequestMapping(value = "/my/list/{task_status}")
    public void list(HttpServletResponse response,
                     @PathVariable(value = "task_status", required = false) String status)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer();
        List<BriefTask> tasks = null;
        if (status == null) {
            tasks = briefTaskService.findByPerformer(performer.getId());
        } else {
            tasks = briefTaskService.findByPerformerAndStatus(performer.getId(), status);
        }
        sendDefaultJson(response, tasks);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void create(@Validated @RequestBody TaskDto dto,
                       HttpServletResponse response) {
        Task task = taskMapper.getEntity(dto);
        taskService.create(task);
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/modify/priority")
    public void changePriority(@RequestParam("priority") String newPriority,
                               @RequestParam("task_id") Integer taskId) {
        Task task = taskService.findOne(taskId);
        task.setPriority(newPriority);
        taskService.update(task);
    }

    @RequestMapping(value = "/modify/status")
    public void changeStatus(@RequestParam("status") String newStatus,
                             @RequestParam("task_id") Integer taskId) {
        Task task = taskService.findOne(taskId);
        //task.setStatus(newStatus);
        taskService.update(task);
    }
}
