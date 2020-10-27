package app.customtenant.eventdriven.listeners;

import app.customtenant.eventdriven.domain.TaskApplicationEvent;
import app.customtenant.eventdriven.domain.TaskEventEnum;
import app.customtenant.eventdriven.service.ITaskApplicationEventService;
import app.tenantconfiguration.TenantContext;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TaskApplicationEventListener
        implements ApplicationListener<TaskApplicationEvent> {

    private EnumMap<TaskEventEnum, List<ITaskApplicationEventService>> servicesMap;

    @Autowired
    public TaskApplicationEventListener(List<ITaskApplicationEventService> services) {
        servicesMap = new EnumMap<TaskEventEnum, List<ITaskApplicationEventService>>(TaskEventEnum.class);
        for (ITaskApplicationEventService service : services) {
            List<ITaskApplicationEventService> list = servicesMap.get(service.getType());
            if (list == null) {
                list = new LinkedList<>();
                servicesMap.put(service.getType(), list);
            }
            list.add(service);
        }
    }

    @Override
    @Async
    public void onApplicationEvent(TaskApplicationEvent event) {
        String tenant = event.getTenant();
        String prevTenant = TenantContext.getTenant();
        List<ITaskApplicationEventService> services = servicesMap.get(event.getType());
        for (ITaskApplicationEventService service : services) {
            service.service(event);
        }
        if (prevTenant == null) {
            prevTenant = TenantContext.DEFAULT_TENANT_IDENTIFIER;
        }
        TenantContext.setTenant(prevTenant);
    }

}
