package app.customtenant.models.basic.taskmodels;

import app.customtenant.eventdriven.publishers.TaskEventPublisher;
import app.customtenant.models.IChanger;
import app.customtenant.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskDeadlineChanger implements IChanger<Task, Boolean> {

    @Autowired
    private TaskEventPublisher publisher;

    @Autowired
    private ITaskService taskService;

    @Override
    public void change(Task task, Boolean deadline) {
        ;
    }
}
