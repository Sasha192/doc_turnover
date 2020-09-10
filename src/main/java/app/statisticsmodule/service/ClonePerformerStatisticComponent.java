package app.statisticsmodule.service;

import app.statisticsmodule.abstr.AbstractPerformerStatistics;
import java.lang.reflect.InvocationTargetException;

public class ClonePerformerStatisticComponent {

    public static AbstractPerformerStatistics clone(AbstractPerformerStatistics stat) {
        try {
            AbstractPerformerStatistics statistics = getInstance(stat);
            statistics.setPerformerId(stat.getPerformerId());
            statistics.setPerformer(stat.getPerformer());
            statistics.setBriefPerformer(stat.getBriefPerformer());
            return statistics;
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException
                | InstantiationException e) {
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
