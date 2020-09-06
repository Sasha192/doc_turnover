package app.statisticsmodule.service;

import app.models.basic.IChanger;
import app.models.basic.Task;
import app.service.interfaces.IPerformerService;
import app.service.interfaces.IStatusService;
import app.service.interfaces.ITaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("deadline_modify")
    private IChanger<Task, Boolean> taskDeadlineIChanger;

    @Override
    public void work() {
        setDeadlines();
    }

    private void setDeadlines() {
        int pageNumber = 1;
        int pageSize = 25;
        List<Task> tasks = null;
        while (null != (tasks
                = taskService.findOnDeadlineDate(pageNumber++, pageSize))) {
            for (Task task : tasks) {
                taskDeadlineIChanger.change(task, true);
            }
        }
    }
}
