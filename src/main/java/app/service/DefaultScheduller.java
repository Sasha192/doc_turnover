package app.service;

import app.configuration.spring.constants.Constants;
import app.statisticsmodule.service.IStatisticManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DefaultScheduller {

    private static final Logger LOGGER = Logger.getLogger("infoLogger");
    private static final String statisticManagerWorkMethodName;
    private static final String updateConstantsMethodName;

    static {
        String className = DefaultScheduller.class.getName();
        statisticManagerWorkMethodName = className + ".statisticManagerWork";
        updateConstantsMethodName = className + ".updateConstants";
    }

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
        LOGGER.debug("RUN THIS METHOD : ");
        LOGGER.debug(statisticManagerWorkMethodName);
        long start = System.currentTimeMillis();
        statisticManager.work();
        long end = System.currentTimeMillis();
        long sec = (end - start) / 1_000;
        long min = sec / 60;
        sec = sec - min * 60;
        long hours = min / 60;
        min = (hours * 60) - min;
        LOGGER.debug(statisticManagerWorkMethodName + "\n" + "LASTED : HOURS = " + hours
                + " MIN = " + min + " SEC = " + sec);
    }

}
