package app.eventdriven.service;

import app.eventdriven.domain.CreationTaskEvent;
import app.eventdriven.domain.GenericApplicationEvent;
import app.eventdriven.domain.TaskEventEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreationTaskEventService
        implements ITaskEventService<CreationTaskEvent> {

    @Override
    public TaskEventEnum getType() {
        return TaskEventEnum.CREATION;
    }

    @Override
    @Transactional
    public void service(GenericApplicationEvent gevent) {
        CreationTaskEvent event = (CreationTaskEvent) gevent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof CreationTaskEventService) {
            return true;
        }
        return false;
    }
}
