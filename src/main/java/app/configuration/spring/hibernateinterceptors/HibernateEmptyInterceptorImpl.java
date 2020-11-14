package app.configuration.spring.hibernateinterceptors;

import java.io.Serializable;

import app.customtenant.eventdriven.publishers.TaskEventPublisher;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.models.basic.taskmodels.TaskStatusValidator;
import app.tenantconfiguration.TenantContext;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("hibernate_empty_int_impl")
public class HibernateEmptyInterceptorImpl extends EmptyInterceptor {

    private final TaskEventPublisher taskEventPublisher;

    @Autowired
    public HibernateEmptyInterceptorImpl(TaskEventPublisher taskEventPublisher) {
        this.taskEventPublisher = taskEventPublisher;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Task) {
            Task o = (Task) entity;
            String tenant = o.getTenantId();
            taskEventPublisher.onCreate(o, tenant);
        }
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        if (entity instanceof Task) {
            processTaskChanges((Task) entity, currentState,
                    previousState, propertyNames);
        }
        return super.onFlushDirty(entity, id, currentState,
                previousState, propertyNames, types);
    }

    private void processTaskChanges(Task entity, Object[] currentState,
                                    Object[] previousState, String[] propertyNames) {
        boolean sameState;
        Object o1;
        Object o2;
        for (int i = 0; i < propertyNames.length; i++) {
            o1 = currentState[i];
            o2 = previousState[i];
            if (o1 == o2) {
                continue;
            }
            sameState = o1 != null && o1.equals(o2);
            if (!sameState) {
                processState(entity, o1, o2, propertyNames[i]);
            }
        }
    }

    private void processState(Task entity, Object current, Object prev,
                              String propertyName) {
        switch (propertyName) {
            case "status": {
                processTaskNewState(entity, (TaskStatus) current, (TaskStatus) prev);
            }
            case "deadline": {
                processTaskDeadlineSet(entity, (Boolean) current);
            }
            default:
                break;
        }
    }

    private void processTaskNewState(Task entity, TaskStatus newState, TaskStatus oldState) {
        if (null != newState) {
            boolean check = TaskStatusValidator
                    .check(oldState, newState);
            if (check) {
                taskEventPublisher.onStatusUpdate(entity, oldState,
                        newState, entity.getTenantId());
            }
        }
    }

    private void processTaskDeadlineSet(Task entity, Boolean current) {
        if (current) {
            taskEventPublisher.onDeadlineSet(entity, TenantContext.getTenant());
        }
    }
}

