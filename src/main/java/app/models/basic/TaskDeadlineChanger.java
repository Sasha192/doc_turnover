package app.models.basic;

import app.eventdriven.publishers.TaskEventPublisher;
import app.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("deadline_modify")
public class TaskDeadlineChanger implements IChanger<Task, Boolean> {

    @Autowired
    private TaskEventPublisher publisher;

    @Autowired
    private ITaskService taskService;

    @Override
    public void change(Task task, Boolean deadline) {
        if (null != task) {
            if (null != task.getId() && deadline) {
                task.setDeadline(deadline);
                publisher.onDeadlineSet(task);
            } else {
                task.setDeadline(deadline);
            }
            taskService.update(task);
        }
    }
}
