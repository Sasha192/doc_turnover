package app.customtenant.statisticsmodule.service;

import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.IPerformerService;
import app.customtenant.service.interfaces.IStatusService;
import app.customtenant.service.interfaces.ITaskService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("count_stat_service_manager")
@Deprecated
public class StatisticManagerService
        implements IStatisticManager {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private static final String ERROR = "ERROR IN "
            + StatisticManagerService.class.getCanonicalName();

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
        int pageSize = 25;
        List<Task> tasks = null;
        long start = System.currentTimeMillis();
        try {
            for (int pageNumber = 1; ; pageNumber++) {
                tasks = taskService.findOnDeadlineDate(pageNumber, pageSize);
                if (tasks != null || (!tasks.isEmpty())) {
                    if (System.currentTimeMillis() - start > 300_000) {
                        Thread.sleep(1_000 * 60 * 5);
                    }
                    for (Task task : tasks) {
                        task.setDeadline(true);
                        taskService.update(task);
                    }
                } else {
                    break;
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
            LOGGER.error(ERROR);
        }
    }
}
