package app.eventdriven.service;

import app.eventdriven.domain.DeadlineTaskEvent;
import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskEventEnum;
import org.springframework.stereotype.Service;

@Service
public class DeadlineTaskEventService
        implements ITaskEventService<DeadlineTaskEvent> {

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.DEADLINE_EXPIRED;
    }

    @Override
    public void service(GenericApplicationEvent gevent) {
        DeadlineTaskEvent event = (DeadlineTaskEvent) gevent;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof DeadlineTaskEventService) {
            return true;
        }
        return false;
    }
}
