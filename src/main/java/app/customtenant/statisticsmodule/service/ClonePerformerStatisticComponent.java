package app.customtenant.statisticsmodule.service;

import app.customtenant.statisticsmodule.abstr.AbstractPerformerStatistics;

import java.lang.reflect.InvocationTargetException;

public class ClonePerformerStatisticComponent {

    public static AbstractPerformerStatistics clone(AbstractPerformerStatistics stat) {
        try {
            AbstractPerformerStatistics statistics = getInstance(stat);
            statistics.setPerformerId(stat.getPerformerId());
            return statistics;
        } catch (InstantiationException
                | InvocationTargetException
                | NoSuchMethodException
                | IllegalAccessException e) {
            return null;
        }
    }

    private static AbstractPerformerStatistics getInstance(
            AbstractPerformerStatistics stat)
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        return stat.getClass()
                .getDeclaredConstructor()
                .newInstance();
    }
}
