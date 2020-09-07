package app.models.basic;

import app.eventdriven.publishers.TaskEventPublisher;
import app.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("task_status_modify")
public class TaskStatusChanger implements IChanger<Task, TaskStatus> {

    @Autowired
    private TaskEventPublisher publisher;

    @Autowired
    private ITaskService service;

    @Override
    public void change(Task task, TaskStatus newState) {
        if (null != newState && null != task) {
            TaskStatus oldState = task.getStatus();
            if (oldState == null) {
                task.setStatus(newState);
                return;
            }
            if (isValidStatus(oldState, newState)) {
                task.setStatus(newState);
                if (null != task.getId()) {
                    publisher.onStatusUpdate(task, oldState, newState);
                }
                service.update(task);
            }
        }
    }

    private boolean isValidStatus(
            TaskStatus oldState, TaskStatus newState
    ) {
        TaskStatus.DefaultStatus oldS = getByName(oldState);
        TaskStatus.DefaultStatus newS = getByName(newState);
        if (oldS == TaskStatus.DefaultStatus.COMPLETED) {
            return false;
        }
        if (newS == TaskStatus.DefaultStatus.NEW) {
            return false;
        }
        return true;
    }

    private TaskStatus.DefaultStatus getByName(TaskStatus status) {
        return TaskStatus.DefaultStatus
                .getByName(status.getName());
    }
}
