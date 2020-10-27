package app.customtenant.service;

import app.configuration.spring.constants.Constants;
import app.customtenant.events.pub.GenericEventPublisher;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.service.interfaces.ITaskService;
import app.tenantconfiguration.TenantContext;
import app.tenantdefault.models.TenantInfoEntity;
import app.utils.CustomAppDateTimeUtil;
import com.mongodb.client.MongoCursor;
import dev.morphia.Datastore;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
// @CHANGE!!!
public class DefaultScheduller {

    private static final Logger LOGGER = Logger.getLogger("infoLogger");
    private static final String statisticManagerWorkMethodName;
    private static final String updateConstantsMethodName;
    private static final String MSG_STRING = "TENANT_ID=%s\n%s\nLASTED : "
            + "HOURS = %d MIN = %d SEC = %d";

    static {
        String className = DefaultScheduller.class.getName();
        statisticManagerWorkMethodName = className + ".statisticManagerWork";
        updateConstantsMethodName = className + ".updateConstants";
    }

    @Autowired
    private Constants constants;

    /*@Autowired
    @Qualifier("count_stat_service_manager")
    private IStatisticManager statisticManager;*/

    @Autowired
    private Datastore datastore;

    @Autowired
    private ITaskService taskService;

    @Autowired
    @Qualifier("task_control_date_publisher")
    private GenericEventPublisher<Task> controlPublisher;

    @Autowired
    @Qualifier("task_deadline_date_publisher")
    private GenericEventPublisher<Task> deadlinePublisher;

    @Scheduled(fixedDelay = 1_000 * 60 * 10)
    public void updateConstants() {
        constants.updateConstants();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void controlDate() {
        String prevTenant = TenantContext.getTenant();
        try (MongoCursor<TenantInfoEntity> cursor = datastore
                .find(TenantInfoEntity.class)
                .iterator()) {
            while (cursor.hasNext()) {
                TenantInfoEntity tenant = cursor.next();
                TenantContext.setTenant(tenant.getTenantId());
                Date nowPlusThreeDays = CustomAppDateTimeUtil.nowPlus(3, ChronoUnit.DAYS);
                Date now = CustomAppDateTimeUtil.now();
                controlDateNotification(nowPlusThreeDays);
                controlDateNotification(now);
            }
            TenantContext.setTenant(prevTenant);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    public void controlDateNotification(Date date)
            throws InterruptedException {
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        List<Task> tasks = null;
        for (int page = 1; ;page++) {
            tasks = taskService.findOnControlDate(page, pageSize, date);
            if (tasks == null || tasks.isEmpty()) {
                break;
            }
            Stream<Task> stream = tasks.stream();
            if (tasks.size() > 10) {
                stream.parallel();
            }
            stream.forEach(task -> {
                controlPublisher.publish(task, null);
            });
            if (page % 10 == 0) {
                Thread.sleep(1_000 * 60 * 5);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void statisticManagerWork() {
        String prevTenant = TenantContext.getTenant();
        try (MongoCursor<TenantInfoEntity> cursor = datastore
                .find(TenantInfoEntity.class)
                .iterator()) {
            while (cursor.hasNext()) {
                TenantInfoEntity tenant = cursor.next();
                TenantContext.setTenant(tenant.getTenantId());
                long start = System.currentTimeMillis();
                statisticManager.work();
                long end = System.currentTimeMillis();
                long sec = (end - start) / 1_000;
                long min = sec / 60;
                sec = sec - min * 60;
                long hours = min / 60;
                min = (hours * 60) - min;
                LOGGER.debug(String.format(MSG_STRING,
                        TenantContext.getTenant(),
                        statisticManagerWorkMethodName,
                        hours, min, sec));
                TenantContext.setTenant(prevTenant);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deadLineDate() {
        String prevTenant = TenantContext.getTenant();
        try (MongoCursor<TenantInfoEntity> cursor = datastore
                .find(TenantInfoEntity.class)
                .iterator()) {
            while (cursor.hasNext()) {
                TenantInfoEntity tenant = cursor.next();
                TenantContext.setTenant(tenant.getTenantId());
                Date nowPlusThreeDays = CustomAppDateTimeUtil.nowPlus(3, ChronoUnit.DAYS);
                Date now = CustomAppDateTimeUtil.now();
                deadLineDateNotification(nowPlusThreeDays);
                deadLineDateNotification(now);
            }
            TenantContext.setTenant(prevTenant);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    public void deadLineDateNotification(Date date)
            throws InterruptedException {
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        List<Task> tasks = null;
        for (int page = 1; ;page++) {
            tasks = taskService.findOnDeadlineDate(page, pageSize, date);
            if (tasks == null || tasks.isEmpty()) {
                break;
            }
            Stream<Task> stream = tasks.stream();
            if (tasks.size() > 10) {
                stream.parallel();
            }
            stream.forEach(task -> {
                deadlinePublisher.publish(task, null);
            });
            if (page % 10 == 0) {
                Thread.sleep(1_000 * 60 * 5);
            }
        }
    }

}
