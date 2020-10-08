package app.customtenant.statisticsmodule.service;

import app.customtenant.statisticsmodule.abstr.AbstractPerformerStatistics;

public interface ICloneStatistic {

    AbstractPerformerStatistics clone(AbstractPerformerStatistics stat);

}
