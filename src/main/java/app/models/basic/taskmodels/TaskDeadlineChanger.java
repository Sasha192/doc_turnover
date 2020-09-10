package app.models.basic.taskmodels;

import app.eventdriven.publishers.TaskEventPublisher;
import app.models.IChanger;
import app.service.interfaces.ITaskService;
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
