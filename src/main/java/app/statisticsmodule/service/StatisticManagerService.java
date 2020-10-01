package app.statisticsmodule.service;

import app.models.basic.taskmodels.Task;
import app.service.interfaces.IPerformerService;
import app.service.interfaces.IStatusService;
import app.service.interfaces.ITaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("count_stat_service_manager")
@Deprecated
public class StatisticManagerService
        implements IStatisticManager {

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IPerformerService performerService;

    @Autowired
    private IStatusService statusService;

    @Override
    public void work() {
        setDeadlines();
    }

    private void setDeadlines() {
        int pageNumber = 1;
        int pageSize = 25;
        List<Task> tasks = null;
        while (null != (tasks
                = taskService.findOnDeadlineDate(pageNumber++, pageSize))
                && (!tasks.isEmpty())) {
            for (Task task : tasks) {
                task.setDeadline(true);
                taskService.update(task);
            }
        }
    }
}
