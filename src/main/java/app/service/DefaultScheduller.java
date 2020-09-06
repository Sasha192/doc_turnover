package app.service;

import app.configuration.spring.constants.Constants;
import app.statisticsmodule.service.IStatisticManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DefaultScheduller {

    @Autowired
    private Constants constants;

    @Autowired
    @Qualifier("count_stat_service_manager")
    private IStatisticManager statisticManager;

    @Scheduled(fixedDelay = 1_000 * 60 * 10)
    public void updateConstants() {
        constants.updateConstants();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void statisticManagerWork() {
        statisticManager.work();
    }

}
