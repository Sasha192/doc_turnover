package app.statisticsmodule.service;

import app.statisticsmodule.abstr.AbstractPerformerStatistics;

public interface ICloneStatistic {

    AbstractPerformerStatistics clone(AbstractPerformerStatistics stat);

}
