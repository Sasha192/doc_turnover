package app.customtenant.statisticsmodule.domain;

import app.configuration.spring.constants.Constants;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.DAILY)
public class DailyPerformerStatistic
        extends AbstractCalendarPerformerStatistic {

    public DailyPerformerStatistic() {
        super(CalendarPerformerEnum.DAILY, Constants.DAY_IN_MS);
    }

}
