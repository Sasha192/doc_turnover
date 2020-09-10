package app.statisticsmodule.domain;

import app.configuration.spring.constants.Constants;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.WEEKLY)
public class WeeklyPerformerStatistics
        extends AbstractCalendarPerformerStatistic {

    public WeeklyPerformerStatistics() {
        super(CalendarPerformerEnum.WEEKLY, Constants.DAY_IN_MS * 7);
    }

}
