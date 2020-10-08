package app.customtenant.eventdriven.listeners;

import app.customtenant.eventdriven.domain.TaskApplicationEvent;
import app.customtenant.eventdriven.domain.TaskEventEnum;
import app.customtenant.eventdriven.service.ITaskEventService;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TaskApplicationEventListener
        implements ApplicationListener<TaskApplicationEvent> {

    private EnumMap<TaskEventEnum, List<ITaskEventService>> servicesMap;

    @Autowired
    public TaskApplicationEventListener(List<ITaskEventService> services) {
        servicesMap = new EnumMap<TaskEventEnum, List<ITaskEventService>>(TaskEventEnum.class);
        for (ITaskEventService service : services) {
            List<ITaskEventService> list = servicesMap.get(service.getType());
            if (list == null) {
                list = new LinkedList<>();
                servicesMap.put(service.getType(), list);
            }
            list.add(service);
        }
    }

    @Override
    public void onApplicationEvent(TaskApplicationEvent event) {
        List<ITaskEventService> services = servicesMap.get(event.getType());
        for (ITaskEventService service : services) {
            service.service(event);
        }
    }

}
