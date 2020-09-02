package app.eventdriven.service;

import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskEventEnum;
import app.eventdriven.domain.TaskStatusModificationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskStatusModificationEventService
        implements ITaskEventService<TaskStatusModificationEvent> {

    @Override
    public TaskEventEnum getType() {
        return null;
    }

    @Override
    @Transactional
    public void service(GenericApplicationEvent gevent) {
        TaskStatusModificationEvent event = (TaskStatusModificationEvent) gevent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof TaskStatusModificationEventService) {
            return true;
        }
        return false;
    }
}
